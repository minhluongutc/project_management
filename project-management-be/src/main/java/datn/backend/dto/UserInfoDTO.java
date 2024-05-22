package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoDTO {
    String id;
    String username;
    String password;
    String settingId;
    String firstName;
    String lastName;
    String contact;
    String email;
    Integer gender;
    String avatarId;
    String coverId;
    Date dateOfBirth;
    String address;
    String createUserId;
    Date createTime;
    String updateUserId;
    Date updateTime;
}
