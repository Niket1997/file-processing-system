package org.ema.records;

import org.ema.entities.TransformationCategory;
import org.ema.enums.TransformationStatus;
import org.ema.enums.TransformationType;

public record TransformationKafkaMessage(
        Long transformationId,

        TransformationStatus transformationStatus,

        FileKafkaMessage file,

        String transformationName,

        TransformationType transformationType,

        Long userId

) {
}
