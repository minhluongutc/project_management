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
    String id;

    @Column(name = "TASK_CODE")
    String taskCode;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "SUBJECT")
    String subject;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "IS_PUBLIC")
    Boolean isPublic;

    @Column(name = "TYPE_ID")
    String typeId;

    @Column(name = "STATUS_ISSUE_ID")
    String statusIssueId;

    @Column(name = "PRIORITY")
    Integer priority;

    @Column(name = "severity")
    Integer severity;

    @Column(name = "PARENT_ID")
    String parentId;

    @Column(name = "ASSIGN_USER_ID")
    String assignUserId;

    @Column(name = "REVIEW_USER_ID")
    String reviewUserId;

    @Column(name = "CATEGORY_ID")
    String categoryId;

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
