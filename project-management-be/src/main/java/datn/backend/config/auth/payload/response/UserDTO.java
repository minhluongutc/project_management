package datn.backend.config.auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class UserDTO {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String avatarId;
    String companyId;
    List<String> roles;
}
