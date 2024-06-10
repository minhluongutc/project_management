package datn.backend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponseDTO {
    String id;
    String toUserId;
    String fromUserId;
    String taskId;
    String taskCode;
    String projectName;
    String content;
    Integer actionType;
    String avatarId;
    String username;
    Date createTime;
    Boolean isRead;
}
