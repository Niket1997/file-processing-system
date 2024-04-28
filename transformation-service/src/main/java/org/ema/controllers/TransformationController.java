package org.ema.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.entities.Transformation;
import org.ema.interfaces.ITransformationService;
import org.ema.records.CreateTransformationRequest;
import org.ema.records.CreateTransformationsResponse;
import org.ema.records.TransformationResponse;
import org.ema.records.UpdateTransformationRequest;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/transformations")
public class TransformationController {
    private final ITransformationService transformationService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public CreateTransformationsResponse createTransformations(@RequestBody List<CreateTransformationRequest> transformationRequests) {
        return transformationService.createTransformations(transformationRequests);
    }

    @PutMapping("/{transformationId}")
    public Transformation updateTransformation(@PathVariable Long transformationId, @RequestBody UpdateTransformationRequest request) {
        log.info("Updating transformation {} with payload {}", transformationId, request);
        return transformationService.updateTransformation(transformationId, request);
    }

    @GetMapping("/{transformationId}")
    public TransformationResponse getTransformation(@PathVariable Long transformationId) {
        return transformationService.getTransformation(transformationId);
    }

    @GetMapping("/user/{userId}")
    public List<TransformationResponse> getTransformationForUser(@PathVariable Long userId) {
        return transformationService.getTransformationForUser(userId);
    }
}
