package datn.backend.service.impl;

import datn.backend.dto.UserDTO;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.UserService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryJPA userRepositoryJPA;

    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Value("${user.default.password}")
    private String defaultPassword;

    public UserServiceImpl(UserRepositoryJPA userRepositoryJPA,
                           ModelMapper modelMapper,
                           PasswordEncoder encoder
    ) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public UserEntity createUser(Authentication authentication, UserDTO.UserRequestDTO dto) {
        UserEntity userEntity = new UserEntity();
        if (userRepositoryJPA.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (userRepositoryJPA.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }
        modelMapper.map(dto, userEntity);
        userEntity.setId(AuditUtils.generateUUID());
        userEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        userEntity.setCreateTime(AuditUtils.createTime());
        userEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        userEntity.setPassword(encoder.encode(defaultPassword));
        userRepositoryJPA.save(userEntity);
        return userEntity;
    }
}
