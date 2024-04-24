package datn.backend.controllers;

import datn.backend.dto.ProjectUserDTO;
import datn.backend.service.ProjectUserService;
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

    @GetMapping(value = "/project-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserInProject(String projectId) {
        Object result = projectUserService.getUsers(projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/project-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addUsersToProject(Authentication authentication,
                                    @RequestBody List<String> userIds,
                                    @RequestBody String projectId) {
        Object result = projectUserService.addUsersToProject(authentication, userIds, projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/project-users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object changeProfessionalLevelProject(Authentication authentication,
                                                 @PathVariable String userId,
                                                 @RequestBody ProjectUserDTO dto) {
        Object result = projectUserService.changeProfessionalLevelInProject(authentication, userId, dto.getProjectId(), dto.getProfessionalLevel());
        return ResponseUtils.getResponseEntity(result);
    }

}
