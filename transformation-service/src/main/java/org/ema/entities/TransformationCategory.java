package org.ema.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ema.enums.TransformationType;
import org.hibernate.annotations.Filter;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "transformation_categories")
@Filter(name = "soft_deleted_filter", condition = "deleted_at IS NULL")
public class TransformationCategory extends BaseEntity {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "transformation_type")
    private TransformationType transformationType;
}
