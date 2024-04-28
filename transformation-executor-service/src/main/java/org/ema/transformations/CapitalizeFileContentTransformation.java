package org.ema.transformations;

import lombok.RequiredArgsConstructor;
import org.ema.aws.S3Uploader;
import org.ema.downloadstrategies.TextFileDownloadStrategy;
import org.ema.interfaces.ITransformationStrategy;
import org.ema.records.TransformationKafkaMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CapitalizeFileContentTransformation implements ITransformationStrategy {
    private final TextFileDownloadStrategy downloadStrategy;
    private final S3Uploader s3Uploader;


    @Override
    public Long transform(TransformationKafkaMessage transformationKafkaMessage) {
        byte[] fileContentBytes = downloadStrategy.download(transformationKafkaMessage.file());
        String fileContent = new String(fileContentBytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileContent.length(); i++) {
            sb.append(Character.toUpperCase(fileContent.charAt(i)));
        }
        System.out.println(sb);

        return s3Uploader.upload(transformationKafkaMessage.userId(), sb.toString());
    }
}
