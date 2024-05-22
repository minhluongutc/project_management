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
@Table(name = "UPDATE_HISTORY_TASK")
public class UpdateHistoryTaskEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "TASK_ID")
    String taskId;

    @Column(name = "OLD_SUBJECT")
    String oldSubject;

    @Column(name = "NEW_SUBJECT")
    String newSubject;

    @Column(name = "OLD_DESCRIPTION")
    String oldDescription;

    @Column(name = "NEW_DESCRIPTION")
    String newDescription;

    @Column(name = "OLD_TYPE_ID")
    String oldTypeId;

    @Column(name = "NEW_TYPE_ID")
    String newTypeId;

    @Column(name = "OLD_STATUS_ISSUE_ID")
    String oldStatusIssueId;

    @Column(name = "NEW_STATUS_ISSUE_ID")
    String newStatusIssueId;

    @Column(name = "OLD_PRIORITY")
    Integer oldPriority;

    @Column(name = "NEW_PRIORITY")
    Integer newPriority;

    @Column(name = "OLD_SEVERITY")
    Integer oldSeverity;

    @Column(name = "NEW_SEVERITY")
    Integer newSeverity;

    @Column(name = "OLD_PARENT_ID")
    String oldParentId;

    @Column(name = "NEW_PARENT_ID")
    String newParentId;

    @Column(name = "OLD_START_DATE")
    Date oldStartDate;

    @Column(name = "NEW_START_DATE")
    Date newStartDate;

    @Column(name = "OLD_DUE_DATE")
    Date oldDueDate;

    @Column(name = "NEW_DUE_DATE")
    Date newDueDate;

    @Column(name = "OLD_ESTIMATE_TIME")
    Integer oldEstimateTime;

    @Column(name = "NEW_ESTIMATE_TIME")
    Integer newEstimateTime;

    @Column(name = "OLD_ASSIGN_USER_ID")
    String oldAssignUserId;

    @Column(name = "NEW_ASSIGN_USER_ID")
    String newAssignUserId;

    @Column(name = "OLD_REVIEW_USER_ID")
    String oldReviewUserId;

    @Column(name = "NEW_REVIEW_USER_ID")
    String newReviewUserId;

    @Column(name = "OLD_CATEGORY_ID")
    String oldCategoryId;

    @Column(name = "NEW_CATEGORY_ID")
    String newCategoryId;

    @Column(name = "MODIFY_TIME")
    Date modifyTime;

    @Column(name = "MODIFY_USER_ID")
    String modifyUserId;
}
