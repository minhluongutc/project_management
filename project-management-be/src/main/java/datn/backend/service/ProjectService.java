package datn.backend.service;

import datn.backend.dto.ProjectDTO;
import org.springframework.security.core.Authentication;

public interface ProjectService {
    Object insertProject(Authentication authentication, ProjectDTO.ProjectInsertDTO dto);
}
