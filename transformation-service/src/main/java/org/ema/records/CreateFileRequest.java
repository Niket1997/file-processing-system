package org.ema.records;


import org.ema.enums.SourceType;

public record CreateFileRequest(

        Long userId,

        String sourceFileId,

        SourceType sourceType,

        String mimeType,

        String sourceFileAuthKey
) {
}
