package org.ema.aws;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.ema.utils.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;
    private final Snowflake snowflake;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public Long upload(Long userId, String content) {
        Long outPutFileId = snowflake.nextId();
        String key = String.format("transformations/%d/%d", userId, outPutFileId);
        amazonS3.putObject(bucket, key, content);
        return outPutFileId;
    }
}
