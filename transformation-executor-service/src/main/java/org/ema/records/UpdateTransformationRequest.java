package org.ema.records;

import org.ema.enums.TransformationStatus;

public record UpdateTransformationRequest(
        TransformationStatus transformationStatus,

        Long outputFileId
) {
}
