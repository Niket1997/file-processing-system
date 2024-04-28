package org.ema.records;

public record CreateTransformationRequest(
        CreateFileRequest file,

        String transformationName
) {
}
