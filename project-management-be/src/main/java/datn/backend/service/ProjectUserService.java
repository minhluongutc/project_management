package datn.backend.service;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;

import java.util.List;

public interface ProjectUserService {
    List<UserDTO.UserResponseDTO> getUsers(Integer projectId);
}
