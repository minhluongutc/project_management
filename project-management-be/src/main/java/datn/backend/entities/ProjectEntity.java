package datn.backend.entities;

import jakarta.persistence.*;
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
@Table(name = "PROJECT")
public class ProjectEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "CODE")
    String code;

    @Column(name = "NAME")
    String name;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "PARENT_ID")
    String parentId;

    @Column(name = "WARNING_TIME")
    Integer warningTime;

    @Column(name = "DANGER_TIME")
    Integer dangerTime;

    @Column(name = "CREATE_USER_ID")
    String createUserId;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "UPDATE_USER_ID")
    String updateUserId;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "ENABLED")
    Integer enabled;
}
