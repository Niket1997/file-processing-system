package org.ema.records;

import org.ema.enums.TransformationType;

public record CreateTransformationCategoryRequest(String transformationName, TransformationType transformationType) {
}
