package org.ema.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.entities.TransformationCategory;
import org.ema.interfaces.ITransformationCategoryService;
import org.ema.records.CreateTransformationCategoryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/transformation-categories")
public class TransformationCategoryController {
    private final ITransformationCategoryService transformationCategoryService;

    @PostMapping
    public TransformationCategory createTransformationCategory(@RequestBody CreateTransformationCategoryRequest request) {
        return transformationCategoryService.createTransformationCategory(request);
    }

    @GetMapping("/{transformationName}")
    public TransformationCategory getTransformationCategory(@PathVariable String transformationName) {
        return transformationCategoryService.getTransformationCategory(transformationName);
    }
}
