package datn.backend.controllers;

import datn.backend.dto.ProjectDTO;
import datn.backend.service.ProjectService;
import datn.backend.service.jpa.ProjectServiceJPA;
import datn.backend.utils.ResponseUtils;
import datn.backend.utils.response.ResponseTreeNode;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectController {
    final ProjectServiceJPA projectServiceJPA;
    final ProjectService projectService;

    @GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProjects(Authentication authentication) {
        Object result = projectServiceJPA.getAllProjects();
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/{userId}/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseTreeNode<Object> getProjectsByUserId(Authentication authentication,
                                                        @PathVariable String userId) {
        Object result = projectService.getProjectsByUserId(userId);
        return ResponseUtils.getTreeResponseEntity(result);
    }

    @GetMapping(value = "/projects/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProject(Authentication authentication, @PathVariable String id) {
        Object result = projectService.getProjectById(id);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertProject(Authentication authentication,
                                                @Valid @RequestBody ProjectDTO.ProjectInsertDTO dto) {
        Object result = projectService.insertProject(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/projects/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProject(Authentication authentication,
                                                @PathVariable String id,
                                                @Valid @RequestBody ProjectDTO.ProjectUpdateDTO dto) {
        Object result = projectService.updateProject(authentication, id, dto);
        return ResponseUtils.getResponseEntity(result);
    }
}
