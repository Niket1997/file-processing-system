package org.ema.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ema.enums.TransformationStatus;
import org.hibernate.annotations.Filter;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "transformations")
@Filter(name = "soft_deleted_filter", condition = "deleted_at IS NULL")
@Table(
        indexes = {
                // get all transformations og a user with certain transformation mame
                @Index(name = "idx_transformations_user_id_transformation_name", columnList = "user_id, transformation_name"),
        }
)
public class Transformation extends BaseEntity {
    @Id
    private Long id;

    @Column(name = "transformation_status")
    private TransformationStatus transformationStatus;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "transformation_name")
    private String transformationName;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "output_file_id")
    private Long outputFileId;
}
