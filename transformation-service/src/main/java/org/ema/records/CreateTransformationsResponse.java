package org.ema.records;

import org.ema.entities.Transformation;

import java.util.List;

public record CreateTransformationsResponse(
        List<Transformation> successful,
        List<CreateTransformationRequest> failed
) {
}
