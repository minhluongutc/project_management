package datn.backend.controllers;

import datn.backend.dto.UserUpdateDTO;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.ProjectUserService;
import datn.backend.service.UserService;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Object> getUserById(Authentication authentication,
                              @PathVariable String id) {
        return ResponseUtils.getResponseEntity(projectUserService.getUserById(authentication, id));
    }

    @PutMapping(value = "/users/{id}",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> updateUser(Authentication authentication,
                                             @PathVariable String id,
                                             @RequestPart("dto") UserUpdateDTO dto,
                                             @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                             @RequestPart(value = "cover", required = false) MultipartFile cover) {
        return ResponseUtils.getResponseEntity(userService.updateUser(authentication, id, dto, avatar, cover));
    }
}
