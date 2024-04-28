package org.ema.downloadstrategies;

import lombok.extern.slf4j.Slf4j;
import org.ema.interfaces.IDownloadStrategy;
import org.ema.records.FileKafkaMessage;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
@Slf4j
public class TextFileDownloadStrategy implements IDownloadStrategy {
    /**
     * This method deals with downloading txt file content
     *
     * @param fileKafkaMessage input object which contains fileId and accessToken
     * @return byte array with all the file content
     */
    @Override
    public byte[] download(FileKafkaMessage fileKafkaMessage) {
        return getFileContent(fileKafkaMessage.sourceFileId(), fileKafkaMessage.sourceFileAuthKey());
    }

    private byte[] getFileContent(String fileId, String accessToken) {
        byte[] fileBytes = null;
        try {
            URI uri = new URI("https://www.googleapis.com/drive/v3/files/" + fileId + "?alt=media");
            URL url = uri.toURL();
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                fileBytes = outputStream.toByteArray();
                outputStream.close();
                inputStream.close();
                log.info("File downloaded successfully for fileId : {}", fileId);
            } else {
                log.error("Error downloading file. Response code: {}", responseCode);
            }
            httpConn.disconnect();
        } catch (IOException | URISyntaxException e) {
            log.error("{} occurred while fetching content from file with file Id : {} ", e.getClass().getSimpleName(), fileId, e);
        }
        return fileBytes;
    }
}
