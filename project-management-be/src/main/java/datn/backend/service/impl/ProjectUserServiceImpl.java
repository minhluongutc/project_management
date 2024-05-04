package datn.backend.service.impl;

import datn.backend.dto.UserDTO;
import datn.backend.entities.ProjectEntity;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.ProjectUserService;
import datn.backend.service.UserService;
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

    final UserService userService;

    final ModelMapper modelMapper;

    @Override
    public List<UserDTO.UserResponseDTO> getUsers(String projectId) {
        List<UserDTO.UserResponseDTO> dtos = new ArrayList<>();
        List<ProjectUserEntity> projectUserEntities = projectUserRepositoryJPA.getProjectUserEntitiesByProjectId(projectId);
        for (ProjectUserEntity projectUserEntity : projectUserEntities) {
            UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(projectUserEntity.getUserId(), Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO.UserResponseDTO userResponseDTO = new UserDTO.UserResponseDTO();
            userResponseDTO.setId(projectUserEntity.getId());
            userResponseDTO.setUsername(userEntity.getUsername());
            userResponseDTO.setEmail(userEntity.getEmail());
            userResponseDTO.setFirstName(userEntity.getFirstName());
            userResponseDTO.setLastName(userEntity.getLastName());
            userResponseDTO.setAvatarId(userEntity.getAvatarId());
            userResponseDTO.setUserId(userEntity.getId());
            userResponseDTO.setProfessionalLevel(projectUserEntity.getProfessionalLevel());
            userResponseDTO.setPermission(projectUserEntity.getPermission());
            dtos.add(userResponseDTO);
        }
        return dtos;
    }

//    @Override
//    @Transactional
//    public void addUserToProject(Authentication authentication, String userId, String projectId, Integer professionalLevel) {
//        UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
//        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
//        ProjectUserEntity projectUserEntity = new ProjectUserEntity();
//        projectUserEntity.setId(AuditUtils.generateUUID());
//        projectUserEntity.setUserId(userId);
//        projectUserEntity.setProjectId(projectId);
//        projectUserEntity.setProfessionalLevel(professionalLevel);
//        projectUserEntity.setCreateUserId(AuditUtils.createUserId(authentication));
//        projectUserEntity.setCreateTime(AuditUtils.createTime());
//        projectRepositoryJPA.getProjectEntitiesByParentIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).forEach(project -> {
//            ProjectUserEntity projectUserEntity1 = new ProjectUserEntity();
//            projectUserEntity1.setId(AuditUtils.generateUUID());
//            projectUserEntity1.setUserId(userId);
//            projectUserEntity1.setProjectId(project.getId());
//            projectUserEntity1.setProfessionalLevel(professionalLevel);
//            projectUserEntity1.setCreateUserId(AuditUtils.createUserId(authentication));
//            projectUserEntity1.setCreateTime(AuditUtils.createTime());
//            projectUserRepositoryJPA.save(projectUserEntity1);
//        });
//        projectUserRepositoryJPA.save(projectUserEntity);
//    }

    @Override
    @Transactional
    public void addUserToProject(Authentication authentication, String userId, String projectId, Integer professionalLevel, Integer permission) {
        UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
        addUserToProjectAndSubProjects(authentication, userId, projectId, professionalLevel, permission);
    }

    private void addUserToProjectAndSubProjects(Authentication authentication, String userId, String projectId, Integer professionalLevel, Integer permission) {
        ProjectUserEntity projectUserEntity = new ProjectUserEntity();
        projectUserEntity.setId(AuditUtils.generateUUID());
        projectUserEntity.setUserId(userId);
        projectUserEntity.setProjectId(projectId);
        projectUserEntity.setProfessionalLevel(professionalLevel);
        projectUserEntity.setPermission(permission);
        projectUserEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        projectUserEntity.setCreateTime(AuditUtils.createTime());
        projectUserRepositoryJPA.save(projectUserEntity);

        List<ProjectEntity> subProjects = projectRepositoryJPA.getProjectEntitiesByParentIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value);
        for (ProjectEntity subProject : subProjects) {
            addUserToProjectAndSubProjects(authentication, userId, subProject.getId(), professionalLevel, permission);
        }
    }

    @Override
    @Transactional
    public Object addUsersToProject(Authentication authentication, ArrayList<String> emails, String projectId, Integer professionalLevel, Integer permission) {
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
        for (String email : emails) {
            String userId = userRepositoryJPA.getIdByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("User not found"));
            addUserToProject(authentication, userId, projectId, professionalLevel, permission);
        }
        return "add user to project success";
    }

    @Override
    @Transactional
    public Object changeProfessionalLevelAndPermissionProject(Authentication authentication, String userId, String projectId, Integer professionalLevel, Integer permission) {
        UserEntity userEntity = userRepositoryJPA.findByIdAndEnabled(userId, Constants.STATUS.ACTIVE.value)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectUserEntity projectUserEntity = projectUserRepositoryJPA.getProjectUserEntityByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new RuntimeException("User not found in project"));
        projectUserEntity.setProfessionalLevel(professionalLevel);
        projectUserEntity.setPermission(permission);
        projectUserRepositoryJPA.save(projectUserEntity);
        return projectUserEntity;
    }

    @Override
    @Transactional
    public Object createAndAddUserToProject(Authentication authentication, List<UserDTO.UserRequestDTO> dtos) {
        for (UserDTO.UserRequestDTO userRequestDTO : dtos) {
            ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(userRequestDTO.getProjectId(), Constants.STATUS.ACTIVE.value)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            UserEntity userEntity = userService.createUser(authentication, userRequestDTO);
            addUserToProject(authentication, userEntity.getId(), userRequestDTO.getProjectId(), userRequestDTO.getProfessionalLevel(), userRequestDTO.getPermission());
        }
        return "create and add user to project success";
    }

    @Override
    public Boolean checkExistsByEmailInProject(String projectId, String email) {
        // get all email in project
        List<String> emails = getUsers(projectId).stream().map(UserDTO.UserResponseDTO::getEmail).toList();
        return emails.contains(email);
    }

    @Override
    public Object getUserById(Authentication authentication, String id) {
        return userRepositoryJPA.getUserById(id);
    }
}
