package org.ema.records;

import org.ema.enums.TransformationStatus;

public record Transformation(
        Long transformationId,

        TransformationStatus transformationStatus,

        Long fileId,

        String transformationName,

        Long userId,

        Long outputFileId,

        Long createdAt,

        Long updatedAt,

        Long deletedAt
) {
}
