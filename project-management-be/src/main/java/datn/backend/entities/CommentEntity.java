package datn.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "COMMENT")
public class CommentEntity {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "OBJECT_ID")
    String objectId;

    @Column(name = "CONTENT")
    String content;

    @Column(name = "TYPE")
    Integer type;

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
