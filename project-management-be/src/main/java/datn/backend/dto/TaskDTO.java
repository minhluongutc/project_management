package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

public class TaskDTO {

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskQueryDTO {
        String projectId;
        String subject;
        String description;
        Boolean isPublic;
        String typeId;
        String statusIssueId;
        Integer priority;
        Integer severity;
        String parentId;
        String assignUserId;
        String reviewUserId;
        Date date;
        Date createTime;
        String createUserId;
        Date updateTime;
        String categoryId;

        Integer statusIssueCode;
        Boolean statusIssueCodeIsEqual;

        String otherTaskId;
        String keyword;

        String sortBy;
        String sortDir;
        Integer pageIndex;
        Integer pageSize;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskInsertDTO {
        String id;
        Integer taskCode;
        String projectId;
        String subject;
        String description;
        Boolean isPublic;
        String typeId;
        String statusIssueId;
        Integer priority;
        Integer severity;
        String parentId;
        String assignUserId;
        String reviewUserId;
        String categoryId;
        Date startDate;
        Date dueDate;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskUpdateDTO {
        String subject;
        String description;
        Boolean isPublic;
        String typeId;
        String statusIssueId;
        Integer priority;
        Integer severity;
        String parentId;
        String assignUserId;
        String reviewUserId;
        String categoryId;
        Date startDate;
        Date dueDate;
        Integer estimateTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskResponseDTO {
        String id;
        String taskCode;
        String projectName;
        String subject;
        String description;
        String typeName;
        String statusIssueName;
        Integer priority;
        Integer severity;
        String parentTaskSubject;
        String assignUserName;
        String reviewUserName;
        String categoryName;
        Date startDate;
        Date dueDate;
        Date createTime;
        Date updateTime;
        String createUserName;
        String updateUserName;
        Boolean isPublic;
        String projectId;
        String projectParentTaskCode;
        String projectParentSubject;
        String parentId;
        Integer progress;
        Integer warningTime;
        Integer dangerTime;
        List<AttachmentDTO.AttachmentResponseDTO> attachments;

        public TaskResponseDTO(String id, String taskCode, String projectName, String subject, String description, String typeName, String statusIssueName, Integer priority, Integer severity, String parentTaskSubject, String assignUserName, String reviewUserName, String categoryName, Date startDate, Date dueDate, Date createTime, Date updateTime, String createUserName, String updateUserName, Boolean isPublic, String projectId) {
            this.id = id;
            this.taskCode = taskCode;
            this.projectName = projectName;
            this.subject = subject;
            this.description = description;
            this.typeName = typeName;
            this.statusIssueName = statusIssueName;
            this.priority = priority;
            this.severity = severity;
            this.parentTaskSubject = parentTaskSubject;
            this.assignUserName = assignUserName;
            this.reviewUserName = reviewUserName;
            this.categoryName = categoryName;
            this.startDate = startDate;
            this.dueDate = dueDate;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.createUserName = createUserName;
            this.updateUserName = updateUserName;
            this.isPublic = isPublic;
            this.projectId = projectId;
        }

        // getTasks
        public TaskResponseDTO(
                String id, String taskCode, String projectName, String subject,
                String description, String typeName, String statusIssueName, Integer priority,
                Integer severity, String parentTaskSubject, String assignUserName,
                String reviewUserName, String categoryName, Date startDate, Date dueDate,
                Date createTime, Date updateTime, String createUserName, String updateUserName,
                Boolean isPublic, String projectId, String projectParentTaskCode, String projectParentSubject,
                String parentId, Integer progress, Integer warningTime, Integer dangerTime) {
            this.id = id;
            this.taskCode = taskCode;
            this.projectName = projectName;
            this.subject = subject;
            this.description = description;
            this.typeName = typeName;
            this.statusIssueName = statusIssueName;
            this.priority = priority;
            this.severity = severity;
            this.parentTaskSubject = parentTaskSubject;
            this.assignUserName = assignUserName;
            this.reviewUserName = reviewUserName;
            this.categoryName = categoryName;
            this.startDate = startDate;
            this.dueDate = dueDate;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.createUserName = createUserName;
            this.updateUserName = updateUserName;
            this.isPublic = isPublic;
            this.projectId = projectId;
            this.projectParentTaskCode = projectParentTaskCode;
            this.projectParentSubject = projectParentSubject;
            this.parentId = parentId;
            this.progress = progress;
            this.warningTime = warningTime;
            this.dangerTime = dangerTime;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskDetailResponseDTO {
        String id;
        String taskCode;
        String projectId;
        String projectName;
        String subject;
        String description;
        Boolean isPublic;
        String typeId;
        String statusIssueId;
        Integer priority;
        Integer severity;
        String parentId;
        String parentSubject;
        String assignUserId;
        String reviewUserId;
        String categoryId;
        Date startDate;
        Date dueDate;
        Integer estimateTime;
        String createUserId;
        String createUserName;
        Date createTime;
        String updateUserId;
        Date updateTime;
        Integer enabled;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskResponseGetChildren {
        String id;
        String taskCode;
        String projectId;
        String subject;
        String description;
        Boolean isPublic;
        String typeId;
        String statusIssueId;
        Integer priority;
        Integer severity;
        String parentId;
        String assignUserId;
        String reviewUserId;
        String categoryId;
        Date startDate;
        Date dueDate;
        Integer progress;
    }


}
