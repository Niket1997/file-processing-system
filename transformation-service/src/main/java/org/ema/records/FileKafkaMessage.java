package org.ema.records;

import org.ema.enums.SourceType;

public record FileKafkaMessage(
        Long fileId,

        Long userId,

        String sourceFileId,

        SourceType sourceType,

        String mimeType,

        String sourceFileAuthKey
) {
}
