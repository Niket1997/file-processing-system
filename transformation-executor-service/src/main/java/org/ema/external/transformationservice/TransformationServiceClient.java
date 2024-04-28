package org.ema.external.transformationservice;

import org.ema.records.Transformation;
import org.ema.records.UpdateTransformationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "transformation-service-client", url = "${external.transformation-service.base-url}")
public interface TransformationServiceClient {
    @PutMapping("${external.transformation-service.routes.update-transformation-route}")
    Transformation updateTransformation(@PathVariable("transformationId") Long transformationId, @RequestBody UpdateTransformationRequest request);
}
