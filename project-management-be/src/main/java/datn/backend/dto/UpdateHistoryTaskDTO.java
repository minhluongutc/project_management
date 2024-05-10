package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateHistoryTaskDTO {
    String id;
    String taskId;
    String oldSubject;
    String newSubject;
    String oldDescription;
    String newDescription;
    String oldType;
    String newType;
    String oldStatusIssue;
    String newStatusIssue;
    Integer oldPriority;
    Integer newPriority;
    Integer oldSeverity;
    Integer newSeverity;
    String oldParent;
    String newParent;
    Date oldStartDate;
    Date newStartDate;
    Date oldDueDate;
    Date newDueDate;
    Integer oldEstimateTime;
    Integer newEstimateTime;
    String oldAssignUser;
    String newAssignUser;
    String oldReviewUser;
    String newReviewUser;
    String oldCategory;
    String newCategory;
    Date modifyTime;
    String modifyUserName;
    String avatarId;
}
