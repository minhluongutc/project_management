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
@Table(name = "PROJECT_USER")
public class ProjectUserEntity implements Serializable {

    @Id
    @Column(name = "PROJECT_ID")
    Integer projectId;

    @Column(name = "USER_ID")
    Integer userId;

//    @Column(name = "IS_TEAM_LEADER")
//    Integer isTeamLeader;

    @Column(name = "PROFESSIONAL_LEVEL")
    Integer professionalLevel;
}
