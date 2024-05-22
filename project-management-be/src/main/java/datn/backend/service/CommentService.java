package datn.backend.service;

import datn.backend.dto.CommentResponseDTO;
import datn.backend.entities.CommentEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    List<CommentResponseDTO> getCommentsByObjectId(String objectId);
    CommentEntity insertComment(Authentication authentication, String content, String objectId);
    CommentEntity updateComment(Authentication authentication, String content, String objectId, String id);
    CommentEntity deleteComment(Authentication authentication, String id);
}
