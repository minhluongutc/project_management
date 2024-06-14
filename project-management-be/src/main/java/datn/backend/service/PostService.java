package datn.backend.service;

import datn.backend.dto.PostInsertDTO;
import datn.backend.dto.PostResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    List<PostResponseDTO> getPosts(Authentication authentication, String projectId);
    Object insertPost(Authentication authentication, PostInsertDTO dto, MultipartFile[] files);
    Object updatePost(Authentication authentication, String id, PostInsertDTO dto);
    Object deletePost(Authentication authentication, String id);
    Object getPost(Authentication authentication, String id);
}
