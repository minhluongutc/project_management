package datn.backend.controllers;

import datn.backend.dto.AddUsersToProjectDTO;
import datn.backend.dto.ProjectUserDTO;
import datn.backend.dto.UserDTO;
import datn.backend.service.ProjectUserService;
import datn.backend.service.jpa.ProjectUserServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUserController {
    final ProjectUserService projectUserService;
    final ProjectUserServiceJPA projectUserServiceJPA;

    @GetMapping(value = "/project-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserInProject(String projectId) {
        Object result = projectUserService.getUsers(projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/project-users/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getRoleByProjectAndUser(String projectId, String userId) {
        Object result = projectUserServiceJPA.getRoleByProjectAndUser(projectId, userId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/project-users", produces = MediaType.APPLICATION_JSON_VALUE)// sai
    public Object addUsersToProject(Authentication authentication,
                                    @RequestBody AddUsersToProjectDTO dto) {
        Object result = projectUserService.addUsersToProject(authentication, dto.getEmails(), dto.getProjectId(), dto.getProfessionalLevel(), dto.getPermission());
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/project-users/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createAndAddUserToProject(Authentication authentication,
                                            @RequestBody List<UserDTO.UserRequestDTO> dtos) {
        Object result = projectUserService.createAndAddUserToProject(authentication, dtos);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/project-users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object changeProfessionalLevelAndPermissionProject(Authentication authentication,
                                                 @PathVariable String userId,
                                                 @RequestBody ProjectUserDTO dto) {
        Object result = projectUserService.changeProfessionalLevelAndPermissionProject(authentication, userId, dto.getProjectId(), dto.getProfessionalLevel(), dto.getPermission());
        return ResponseUtils.getResponseEntity(result);
    }

}
