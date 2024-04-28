package org.ema.repositories;

import org.ema.entities.File;
import org.springframework.data.repository.CrudRepository;

public interface IFileRepository extends CrudRepository<File, Long> {
}
