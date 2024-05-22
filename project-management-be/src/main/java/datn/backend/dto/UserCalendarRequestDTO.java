package datn.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendarRequestDTO {
    String id;
    String projectId;
    String taskId;
    String title;
    String description;
    Date start;
    Date end;
    Boolean allDay;
    Boolean changeDateOnly = false;
}
