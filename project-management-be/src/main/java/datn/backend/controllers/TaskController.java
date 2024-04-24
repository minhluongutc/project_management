package datn.backend.controllers;

import datn.backend.dto.TaskDTO;
import datn.backend.service.TaskService;
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
public class TaskController {
    final TaskService taskService;

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTasks(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        Object result = taskService.getTasks(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/tasks/level", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        Object result = taskService.getTasksAccordingLevel(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/tasks/{id}/children", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTasksChildren(Authentication authentication, @PathVariable String id) {
        Object result = taskService.getTasksChildrenByParentId(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/tasks", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> insertTask(Authentication authentication,
                                             @RequestPart("dto") TaskDTO.TaskInsertDTO dto,
                                             @RequestPart(value = "files", required = false) MultipartFile[] files) {
        Object result = taskService.insertTask(authentication, dto, files);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTask(Authentication authentication,
                                             @RequestBody TaskDTO.TaskUpdateDTO dto,
                                             @PathVariable String id) {
        Object result = taskService.updateTask(authentication, dto, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTask(Authentication authentication, @PathVariable String id) {
        Object result = taskService.getTask(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
