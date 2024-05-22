package datn.backend.dto;

import jakarta.validation.constraints.NotBlank;
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
public class FilterInsertDTO {
    @NotBlank
    String name;
    String subject;
    @NotBlank
    String projectId;
    Boolean priorityIsEqual;
    String priority;
    Boolean severityIsEqual;
    String severity;
    Boolean typeIdIsEqual;
    String typeId;
    Boolean statusIssueIdIsEqual;
    String statusIssueId;
    Boolean categoryIdIsEqual;
    String category;
    Boolean assignUserIdIsEqual;
    String assignUserId;
    Boolean reviewUserIdIsEqual;
    String reviewUserId;
    String startDateOperator;
    Date startDate;
    String endDateOperator;
    Date endDate;
}
