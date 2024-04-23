package datn.backend.service;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectUserService {
    List<UserDTO.UserResponseDTO> getUsers(String projectId);
    void addUserToProject(Authentication authentication, String userId, String projectId);
    Object addUsersToProject(Authentication authentication, List<String> userIds, String projectId);
    Object changeProfessionalLevelInProject(Authentication authentication, String userId, String projectId, Integer professionalLevel);
}
