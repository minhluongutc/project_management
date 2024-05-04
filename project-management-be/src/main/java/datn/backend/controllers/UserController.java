package datn.backend.controllers;

import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.ProjectUserService;
import datn.backend.service.UserService;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserRepositoryJPA userRepositoryJPA;
    final UserService userService;
    final ProjectUserService projectUserService;

    @GetMapping(value = "/users/exist-username", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object checkExistUsername(String username) {
        return userRepositoryJPA.existsByUsername(username);
    }

    @GetMapping(value = "/users/exist-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object checkExistEmail(String email) {
        return userRepositoryJPA.existsByEmail(email);
    }

    @GetMapping(value = "/users/exist-email/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object checkExistEmailInProjectId(@PathVariable String projectId,
                                             String email
    ) {
        return projectUserService.checkExistsByEmailInProject(projectId, email);
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserById(Authentication authentication,
                              @PathVariable String id) {
        return ResponseUtils.getResponseEntity(projectUserService.getUserById(authentication, id));
    }
}
