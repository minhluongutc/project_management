package datn.backend.controllers;

import datn.backend.dto.TaskDTO;
import datn.backend.dto.UploadMessageDTO;
import datn.backend.service.TaskService;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import datn.backend.utils.excel.ExcelHelpers;
import datn.backend.utils.exceptions.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @GetMapping(value = "/tasks/{projectId}/import/template")
    public void downloadTemplate(Authentication authentication, @PathVariable String projectId, HttpServletResponse response) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        response.setHeader("Content-disposition", "attachment;filename=Template_ImportTask_" + currentDateTime + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        taskService.downloadTemplate(authentication, projectId, response);
    }

    @PostMapping(value = "/tasks/{projectId}/import", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> importTasks(Authentication authentication,
                                              @PathVariable String projectId,
                                              @RequestPart("file") MultipartFile file) {
        if (!ExcelHelpers.hasExcelFormat(file)) throw new CustomException("Định dạng cho phép .xlsx");
        UploadMessageDTO message = taskService.importTasks(authentication, projectId, file);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

//    @GetMapping(value = "/tasks/statistics/user/{userId}/task-completion-rate/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> getTaskCompletionRate(Authentication authentication,
//                                                        @PathVariable String userId,
//                                                        @PathVariable String projectId) {
//        Object result = taskService.getTaskCompletionRate(authentication, userId);
//        return ResponseUtils.getResponseEntity(result);
//    }
}
