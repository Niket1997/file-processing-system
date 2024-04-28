package org.ema.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Filter;
import org.ema.enums.SourceType;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "files")
@Filter(name = "soft_deleted_filter", condition = "deleted_at IS NULL")
@Table(
        indexes = {
                @Index(name = "idx_files_user_id", columnList = "user_id"), // get all files from user
        }
)
public class File extends BaseEntity {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "source_file_id")
    private String sourceFileId;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "source_file_auth_key")
    private String sourceFileAuthKey;
}
