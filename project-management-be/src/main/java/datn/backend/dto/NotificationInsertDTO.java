package datn.backend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationInsertDTO {
    String id;
    String toUserId;
    String fromUserId;
    String taskId;
    String content;
    Integer actionType;
}
