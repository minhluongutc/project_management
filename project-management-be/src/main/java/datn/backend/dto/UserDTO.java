package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

public class UserDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserResponseDTO {
        String id;
        String userId;
        String username;
        String email;
        String firstName;
        String lastName;
        String avatarId;
        String role;
        Integer professionalLevel;
        Integer permission;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserRequestDTO {
        @NotBlank
        String username;
        @NotBlank
        @Email
        String email;
        String firstName;
        String lastName;
        @NotBlank
        Integer permission;
        String projectId;
        Integer professionalLevel;
        Date dateOfBirth;
    }
}
