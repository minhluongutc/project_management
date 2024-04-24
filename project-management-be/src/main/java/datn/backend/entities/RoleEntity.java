package datn.backend.entities;

import datn.backend.config.auth.ERole;
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
@Table(name = "ROLE")
public class RoleEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME")
    ERole name;

    @Column(name = "DESCRIPTION")
    String description;

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
