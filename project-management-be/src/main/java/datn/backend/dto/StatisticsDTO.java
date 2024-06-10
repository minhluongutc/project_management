package datn.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsDTO {
//    Float taskDoneInTime;
//    Float taskLateNotDone;
//    Float taskLateDone;
//    Float taskToDo;
    Integer taskDoneInTime;
    Integer taskLateNotDone;
    Integer taskLateDone;
    Integer taskToDo;
}
