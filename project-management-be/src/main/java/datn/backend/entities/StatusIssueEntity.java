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
@Table(name = "STATUS_ISSUE")
public class StatusIssueEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "NAME")
    String name;

    @Column(name = "code")
    Integer code;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "PROGRESS")
    Integer progress;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "CREATE_USER_ID")
    String createUserId;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "UPDATE_USER_ID")
    String updateUserId;

    @Column(name = "ENABLED")
    Integer enabled;
}
