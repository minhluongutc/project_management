package datn.backend.service;

import datn.backend.dto.UserDTO;
import datn.backend.dto.UserUpdateDTO;
import datn.backend.entities.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserEntity createUser(Authentication authentication, UserDTO.UserRequestDTO dto);

    Object updateUser(Authentication authentication, String id, UserUpdateDTO dto, MultipartFile avatar, MultipartFile cover);
}
