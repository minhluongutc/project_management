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
import java.util.Date;

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

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "CREATE_TIME")
    Date createTime;
}
