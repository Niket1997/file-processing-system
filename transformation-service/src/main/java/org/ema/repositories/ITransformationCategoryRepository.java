package org.ema.repositories;

import org.ema.entities.TransformationCategory;
import org.springframework.data.repository.CrudRepository;

public interface ITransformationCategoryRepository extends CrudRepository<TransformationCategory, String> {
}
