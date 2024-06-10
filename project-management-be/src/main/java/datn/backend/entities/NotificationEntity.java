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
@Table(name = "NOTIFICATION")
public class NotificationEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "TASK_ID")
    String taskId;

    @Column(name = "ACTION_TYPE")
    Integer type;

    @Column(name = "IS_READED")
    Boolean isRead;

    @Column(name = "OPTIONAL_CONTENT")
    String optionalContent;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "CREATE_USER_ID")
    String createUserId;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "ENABLED")
    Integer enabled;
}
