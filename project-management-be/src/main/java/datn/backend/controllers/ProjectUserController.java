package datn.backend.controllers;

import datn.backend.service.ProjectUserService;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUserController {
    final ProjectUserService projectUserService;

    @GetMapping(value = "/project-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getProjectUsers(String projectId) {
        Object result = projectUserService.getUsers(projectId);
        return ResponseUtils.getResponseEntity(result);
    }
}
