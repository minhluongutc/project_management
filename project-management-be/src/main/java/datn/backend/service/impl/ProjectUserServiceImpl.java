package datn.backend.service.impl;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectEntity;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.ProjectUserService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUserServiceImpl implements ProjectUserService {
    final ProjectUserRepositoryJPA projectUserRepositoryJPA;
    final UserRepositoryJPA userRepositoryJPA;
    final ProjectRepositoryJPA projectRepositoryJPA;
    final ModelMapper modelMapper;

    @Override
    public List<UserDTO.UserResponseDTO> getUsers(String projectId) {
        List<UserDTO.UserResponseDTO> dtos = new ArrayList<>();
        List<ProjectUserEntity> projectUserEntities = projectUserRepositoryJPA.getProjectUserEntitiesByProjectId(projectId);
        for (ProjectUserEntity projectUserEntity : projectUserEntities) {
            UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(projectUserEntity.getUserId(), Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO.UserResponseDTO userResponseDTO = new UserDTO.UserResponseDTO();
            userResponseDTO.setUsername(userEntity.getUsername());
            userResponseDTO.setEmail(userEntity.getEmail());
            userResponseDTO.setFirstName(userEntity.getFirstName());
            userResponseDTO.setLastName(userEntity.getLastName());
            userResponseDTO.setAvatarId(userEntity.getAvatarId());
            userResponseDTO.setUserId(userEntity.getId());
            userResponseDTO.setProfessionalLevel(projectUserEntity.getProfessionalLevel());
            dtos.add(userResponseDTO);
        }
        return dtos;
    }

    @Override
    @Transactional
    public void addUserToProject(Authentication authentication, String userId, String projectId) {
        UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectUserEntity projectUserEntity = new ProjectUserEntity();
        projectUserEntity.setUserId(userId);
        projectUserEntity.setProjectId(projectId);
        projectUserEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        projectUserEntity.setCreateTime(AuditUtils.createTime());
        projectUserRepositoryJPA.save(projectUserEntity);
    }

    @Override
    @Transactional
    public Object addUsersToProject(Authentication authentication, List<String> userIds, String projectId) {
        projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
        for (String userId : userIds) {
            userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
            addUserToProject(authentication, userId, projectId);
        }
        return "add user to project success";
    }

    @Override
    @Transactional
    public Object changeProfessionalLevelInProject(Authentication authentication, String userId, String projectId, Integer professionalLevel) {
        UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectUserEntity projectUserEntity = projectUserRepositoryJPA.getProjectUserEntityByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new RuntimeException("User not found in project"));
        projectUserEntity.setProfessionalLevel(professionalLevel);
        projectUserRepositoryJPA.save(projectUserEntity);
        return projectUserEntity;
    }
}
