package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
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
        Integer projectId;
        String subject;
        String description;
        Boolean isPublic;
        Integer typeId;
        Integer statusIssueId;
        Integer priority;
        Integer severity;
        Integer parentId;
        Integer assignUserId;
        Integer reviewUserId;
        Date date;
        Date createTime;
        String createUserId;
        Date updateTime;
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
        Integer id;
        Integer taskCode;
        Integer projectId;
        String subject;
        String description;
        Boolean isPublic;
        Integer typeId;
        Integer statusIssueId;
        Integer priority;
        Integer severity;
        Integer parentId;
        Integer assignUserId;
        Integer reviewUserId;
        Date startDate;
        Date dueDate;
    }
}
