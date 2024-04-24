package datn.backend.service;

import datn.backend.dto.StatusIssueDTO;
import org.springframework.security.core.Authentication;

public interface StatusIssueService {
    Object insertStatusIssue(Authentication authentication, StatusIssueDTO.StatusIssueRequestDTO dto);
    Object updateStatusIssue(Authentication authentication, StatusIssueDTO.StatusIssueRequestDTO dto, String id);
    Object deleteStatusIssue(Authentication authentication, String id);
}
