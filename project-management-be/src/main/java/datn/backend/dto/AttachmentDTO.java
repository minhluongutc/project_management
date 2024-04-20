package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

public class AttachmentDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AttachmentResponseDTO {
            String id;
            String fileName;
            String filePath;
            Long fileSize;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AttachmentResponsePrimengDTO {
        String id;
        String name;
        String filePath;
        Long size;
        String type;

        public AttachmentResponsePrimengDTO(String id, String name, String filePath, Long size) {
            this.id = id;
            this.name = name;
            this.filePath = filePath;
            this.size = size;
        }
    }
}
