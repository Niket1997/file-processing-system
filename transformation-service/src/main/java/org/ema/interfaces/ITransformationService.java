package org.ema.interfaces;

import org.ema.entities.Transformation;
import org.ema.records.CreateTransformationRequest;
import org.ema.records.CreateTransformationsResponse;
import org.ema.records.TransformationResponse;
import org.ema.records.UpdateTransformationRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface ITransformationService {
    Transformation createTransformation(CreateTransformationRequest request);

    CreateTransformationsResponse createTransformations(List<CreateTransformationRequest> transformationRequests);

    Transformation updateTransformation(Long transformationId, UpdateTransformationRequest request);

    TransformationResponse getTransformation(Long transformationId);

    List<TransformationResponse> getTransformationForUser(Long userId);
}
