package org.ema.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.entities.TransformationCategory;
import org.ema.exceptions.TransformationCategoryNotFoundException;
import org.ema.interfaces.ITransformationCategoryService;
import org.ema.records.CreateTransformationCategoryRequest;
import org.ema.repositories.ITransformationCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransformationCategoryService implements ITransformationCategoryService {
    private final ITransformationCategoryRepository transformationCategoryRepository;

    @Override
    public TransformationCategory createTransformationCategory(CreateTransformationCategoryRequest request) {
        TransformationCategory transformationCategory = new TransformationCategory();
        transformationCategory.setName(request.transformationName());
        transformationCategory.setTransformationType(request.transformationType());
        return transformationCategoryRepository.save(transformationCategory);
    }

    @Override
    public TransformationCategory getTransformationCategory(String name) {
        Optional<TransformationCategory> transformationCategory = transformationCategoryRepository.findById(name);
        if (transformationCategory.isPresent()) {
            return transformationCategory.get();
        }
        throw new TransformationCategoryNotFoundException(name);
    }
}
