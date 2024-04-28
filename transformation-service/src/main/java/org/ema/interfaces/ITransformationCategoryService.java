package org.ema.interfaces;

import org.ema.entities.TransformationCategory;
import org.ema.records.CreateTransformationCategoryRequest;

public interface ITransformationCategoryService {
    TransformationCategory createTransformationCategory(CreateTransformationCategoryRequest request);

    TransformationCategory getTransformationCategory(String name);
}
