package datn.backend.service;

import datn.backend.dto.UserDTO;
import datn.backend.entities.UserEntity;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserEntity createUser(Authentication authentication, UserDTO.UserRequestDTO dto);
}
