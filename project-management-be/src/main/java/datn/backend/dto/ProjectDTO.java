package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import datn.backend.entities.ProjectEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

public class ProjectDTO {

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProjectInsertDTO {
        @NotBlank
        String name;
        String description;
        String parentId;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProjectUpdateDTO {
        @NotBlank
        String name;
        String description;
        Integer warningTime;
        Integer dangerTime;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProjectQueryDTO {
        String name;
        String parentId;
        Integer pageIndex;
        Integer pageSize;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProjectResponseDTO {
        String id;
        String code;
        String name;
        String description;
        String parentId;
        Integer warningTime;
        Integer dangerTime;
        String createUserId;
        Date createTime;
        String updateUserId;
        Date updateTime;
        Integer enabled;
        List<StatusDTO> statusPercents;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StatusDTO {
        String name;
        Double percent;
        String fraction;
    }
}
