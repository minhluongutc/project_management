package datn.backend.entities;

import jakarta.persistence.*;
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
@Table(name = "TASK")
public class TaskEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "TASK_ID")
    Integer taskCode;

    @Column(name = "PROJECT_ID")
    Integer projectId;

    @Column(name = "SUBJECT")
    String subject;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "IS_PUBLIC")
    Boolean isPublic;

    @Column(name = "TYPE_ID")
    Integer typeId;

    @Column(name = "STATUS_ISSUE_ID")
    Integer statusIssueId;

    @Column(name = "PRIORITY")
    Integer priority;

    @Column(name = "PARENT_ID")
    Integer parentId;

    @Column(name = "ASSIGN_USER_ID")
    Integer assignUserId;

    @Column(name = "REVIEW_USER_ID")
    Integer reviewUserId;

    @Column(name = "START_DATE")
    Date startDate;

    @Column(name = "DUE_DATE")
    Date dueDate;

    @Column(name = "ESTIMATE_TIME")
    Integer estimateTime;

    @Column(name = "CREATE_USER_ID")
    String createUserId;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "UPDATE_USER_ID")
    String updateUserId;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "ENABLED")
    Integer enabled;
}
