package datn.backend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponseDTO {
    String id;
    String title;
    String content;
    String projectId;
    String createUserId;
    Date createTime;
    String updateUserId;
    Date updateTime;
    String avatarId;
    String firstName;
    String lastName;
    String username;
    List<AttachmentDTO.AttachmentResponseDTO> attachments;

    public PostResponseDTO(String id, String title, String content, String projectId, String createUserId, Date createTime, String updateUserId, Date updateTime, String avatarId, String firstName, String lastName, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.projectId = projectId;
        this.createUserId = createUserId;
        this.createTime = createTime;
        this.updateUserId = updateUserId;
        this.updateTime = updateTime;
        this.avatarId = avatarId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

}
