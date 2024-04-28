package org.ema.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.ema.exceptions.InvalidRequestException;
import org.ema.redis.MessageEventListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SocketModule {
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final MessageEventListener messageEventListener;

    public SocketModule(
            SocketIOServer socketIOServer,
            RedisMessageListenerContainer redisMessageListenerContainer,
            MessageEventListener messageEventListener) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.messageEventListener = messageEventListener;
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            if (!params.containsKey("userId") || params.get("userId").size() != 1) {
                client.disconnect();
                throw new InvalidRequestException("Invalid user id");
            }
            String userId = params.get("userId").get(0);
            client.set("userId", userId);
            ChannelTopic channelTopic = new ChannelTopic(userId);
            redisMessageListenerContainer.addMessageListener(messageEventListener, channelTopic);
            log.info("## client with userId {} connected. ##", userId);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            String userId = client.get("userId");
            redisMessageListenerContainer.removeMessageListener(messageEventListener, new ChannelTopic(userId));
            log.info("** client with userId {} disconnected. **", userId);
        };
    }
}
