package org.ema.repositories;

import org.ema.entities.Transformation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ITransformationRepository extends CrudRepository<Transformation, Long> {
    List<Transformation> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
