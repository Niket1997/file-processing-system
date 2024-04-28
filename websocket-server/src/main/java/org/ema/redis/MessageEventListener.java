package org.ema.redis;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.records.TransformationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageEventListener implements MessageListener {
    @Value("${socket.client.event.transformation-complete}")
    private String transformationCompleteEvent;

    private final ObjectMapper objectMapper;

    private final SocketIOServer socketIOServer;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            TransformationResponse transformation = objectMapper.readValue(message.getBody(), TransformationResponse.class);
            log.info("transformation event is: {}", transformation);

            Optional<SocketIOClient> receiver = socketIOServer.
                    getAllClients().
                    stream().
                    filter(it -> it.get("userId").equals(String.valueOf(transformation.userId()))).
                    findFirst();

            receiver.ifPresent(client -> {
                client.sendEvent(transformationCompleteEvent, transformation);
            });

        } catch (Exception e) {
            log.error("error while parsing message");
        }
    }
}
