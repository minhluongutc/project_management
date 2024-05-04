package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
}
