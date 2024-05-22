package datn.backend.service;

import datn.backend.dto.ProjectDTO;
import datn.backend.dto.TreeDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {
    List<TreeDTO> getProjectsByUserId(String userId);
    Object insertProject(Authentication authentication, ProjectDTO.ProjectInsertDTO dto);
    Object getProjectById(String id);
    Object updateProject(Authentication authentication, String id, ProjectDTO.ProjectUpdateDTO dto);
}
