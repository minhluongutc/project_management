package datn.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendarResponseDTO {
    String id;
    String userId;
    String projectId;
    String taskId;
    String title;
    String description;
    Date start;
    Date end;
    Boolean allDay;
    Integer statusCode;
}
