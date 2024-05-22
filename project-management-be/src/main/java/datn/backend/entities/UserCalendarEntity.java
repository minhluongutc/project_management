package datn.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "USER_CALENDAR")
public class UserCalendarEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "TASK_ID")
    String taskId;

    @Column(name = "TITLE")
    String title;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "START")
    Date start;

    @Column(name = "END")
    Date end;

    @Column(name = "ALL_DAY")
    Boolean allDay;
}
