package datn.backend.service;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

public interface ProjectUserService {
    List<UserDTO.UserResponseDTO> getUsers(String projectId);
    void addUserToProject(Authentication authentication, String userId, String projectId, Integer professionalLevel, Integer permission);
    Object addUsersToProject(Authentication authentication, ArrayList<String> emails, String projectId, Integer professionalLevel, Integer permission);
    Object changeProfessionalLevelAndPermissionProject(Authentication authentication, String userId, String projectId, Integer professionalLevel, Integer permission);
    Object createAndAddUserToProject(Authentication authentication, List<UserDTO.UserRequestDTO> dto);
    Boolean checkExistsByEmailInProject(String projectId, String email);
    Object getUserById(Authentication authentication, String id);
}
