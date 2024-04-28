package org.ema.records;

import org.ema.enums.TransformationStatus;

public record TransformationResponse(
        Long transformationId,

        TransformationStatus transformationStatus,

        Long fileId,

        String transformationName,

        Long userId,

        Long outputFileId,

        String cdnUrl
) {
}
