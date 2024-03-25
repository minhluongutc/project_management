package datn.backend.config.auth.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SignupRequest {

    String firstName;
    String lastName;
    String contact;
    Integer gender;
    Date dateOfBirth;
    String address;

    @NotBlank
    @Size(min = 3, max = 20)
     String username;

    @NotBlank
    @Size(max = 50)
    @Email
     String email;

     Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
     String password;

}