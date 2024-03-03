package datn.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "project_user")
public class ProjectUserEntity implements Serializable {

    @Id
    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "ROLE_ID")
    String roleId;

    @Column(name = "PROFESSIONAL_LEVEL")
    Integer professionalLevel;
}
