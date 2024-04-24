package datn.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    Object addAttachment(Authentication authentication, String objectId, MultipartFile file, Integer type);

    Object addAttachments(Authentication authentication, String objectId, MultipartFile[] files, Integer type);

    Object deleteAttachment(Authentication authentication, String id);
}
