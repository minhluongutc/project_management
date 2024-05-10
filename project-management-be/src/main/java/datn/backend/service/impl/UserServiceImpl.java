package datn.backend.service.impl;

import datn.backend.dto.AttachmentDTO;
import datn.backend.dto.UserDTO;
import datn.backend.dto.UserUpdateDTO;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.DocumentService;
import datn.backend.service.UserService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final DocumentService documentService;

    private final UserRepositoryJPA userRepositoryJPA;
    private final DocumentRepositoryJPA documentRepositoryJPA;

    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Value("${user.default.password}")
    private String defaultPassword;

    public UserServiceImpl(UserRepositoryJPA userRepositoryJPA,
                           DocumentRepositoryJPA documentRepositoryJPA,
                           DocumentService documentService,
                           ModelMapper modelMapper,
                           PasswordEncoder encoder
    ) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.documentRepositoryJPA = documentRepositoryJPA;
        this.documentService = documentService;
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

    @Override
    @Transactional
    public Object updateUser(Authentication authentication, String id, UserUpdateDTO dto, MultipartFile avatar, MultipartFile cover) {
        UserEntity userEntity = userRepositoryJPA.findById(id).orElse(null);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setContact(dto.getContact());
        userEntity.setAddress(dto.getAddress());
        userEntity.setGender(dto.getGender());
        userEntity.setDateOfBirth(dto.getDateOfBirth());
        userEntity.setUpdateUserId(AuditUtils.createUserId(authentication));
        userEntity.setUpdateTime(AuditUtils.createTime());
        userRepositoryJPA.save(userEntity);

        List<AttachmentDTO.AttachmentResponseDTO> avatarAttachments = documentRepositoryJPA.getAttachmentsByObjectIdAndType(id, Constants.DOCUMENT_TYPE.AVATAR.value);
        List<AttachmentDTO.AttachmentResponseDTO> coverAttachments = documentRepositoryJPA.getAttachmentsByObjectIdAndType(id, Constants.DOCUMENT_TYPE.COVER.value);

        if (avatar != null) {
            if (avatarAttachments != null && !avatarAttachments.isEmpty())
                documentService.deleteAttachment(authentication, avatarAttachments.get(0).getId());
            documentService.addAttachment(authentication, id, avatar, Constants.DOCUMENT_TYPE.AVATAR.value);
        }
        // todo remove avatar
//        else {
//            if (avatarAttachments != null && !avatarAttachments.isEmpty())
//                documentService.deleteAttachment(authentication, avatarAttachments.get(0).getId());
//        }

        if (cover != null) {
            if (coverAttachments != null && !coverAttachments.isEmpty())
                documentService.deleteAttachment(authentication, coverAttachments.get(0).getId());
            documentService.addAttachment(authentication, id, cover, Constants.DOCUMENT_TYPE.COVER.value);
        }
//        else {
//            if (coverAttachments != null && !coverAttachments.isEmpty())
//                documentService.deleteAttachment(authentication, coverAttachments.get(0).getId());
//        }

        return "Update user success";
    }
}
