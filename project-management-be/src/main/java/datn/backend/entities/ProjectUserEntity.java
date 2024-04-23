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
    @Column(name = "ID")
    String id;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "USER_ID")
    String userId;

//    @Column(name = "IS_TEAM_LEADER")
//    Integer isTeamLeader;

    @Column(name = "PROFESSIONAL_LEVEL")
    Integer professionalLevel;

    @Column(name = "CREATE_USER_ID")
    String createUserId;

    @Column(name = "CREATE_TIME")
    Date createTime;
}
