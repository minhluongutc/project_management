package datn.backend.service.impl;

import datn.backend.dto.CommentResponseDTO;
import datn.backend.entities.CommentEntity;
import datn.backend.repositories.jpa.CommentRepositoryJPA;
import datn.backend.service.CommentService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {
    final CommentRepositoryJPA commentRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public List<CommentResponseDTO> getCommentsByObjectId(String objectId) {
        return commentRepositoryJPA.getCommentEntitiesByObjectIdAndEnabled(objectId);
    }

    @Override
    public CommentEntity insertComment(Authentication authentication, String content, String objectId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(AuditUtils.generateUUID());
        commentEntity.setObjectId(objectId);
        commentEntity.setContent(content);
        commentEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        commentEntity.setCreateUserId(AuditUtils.getUserId(authentication));
        commentEntity.setCreateTime(AuditUtils.createTime());
        commentRepositoryJPA.save(commentEntity);
        return commentEntity;
    }

    @Override
    public CommentEntity updateComment(Authentication authentication, String content, String objectId, String id) {
        Optional<CommentEntity> commentEntity = commentRepositoryJPA.findById(id);

        if (commentEntity.isEmpty()) throw new RuntimeException("Comment not found");

        CommentEntity comment = commentEntity.get();
        comment.setContent(content);
        comment.setUpdateUserId(AuditUtils.getUserId(authentication));
        comment.setUpdateTime(AuditUtils.createTime());
        commentRepositoryJPA.save(comment);

        return comment;
    }

    @Override
    public CommentEntity deleteComment(Authentication authentication, String id) {
        Optional<CommentEntity> commentEntity = commentRepositoryJPA.findById(id);
        if (commentEntity.isEmpty()) throw new RuntimeException("Comment not found");
        CommentEntity comment = commentEntity.get();
        comment.setEnabled(Constants.STATUS.IN_ACTIVE.value);
        return comment;
    }
}
