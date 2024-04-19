package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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

        Integer statusIssueCode;
        Boolean statusIssueCodeIsEqual;

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
    }
}
