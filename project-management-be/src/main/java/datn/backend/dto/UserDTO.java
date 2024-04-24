package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

public class UserDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserResponseDTO {
        String userId;
        String username;
        String email;
        String firstName;
        String lastName;
        String avatarId;
        String role;
        Integer professionalLevel;
    }
}
