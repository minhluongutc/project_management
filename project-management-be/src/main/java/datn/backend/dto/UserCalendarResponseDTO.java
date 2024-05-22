package datn.backend.dto;

import jakarta.persistence.Column;
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
}
