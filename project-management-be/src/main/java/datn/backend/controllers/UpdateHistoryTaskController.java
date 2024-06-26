package datn.backend.controllers;

import datn.backend.service.jpa.UpdateHistoryTaskServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateHistoryTaskController {
    final UpdateHistoryTaskServiceJPA updateHistoryTaskServiceJPA;

    @GetMapping(value = "/update-history-task", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUpdateHistoryTaskEntitiesByTaskId(Authentication authentication, String taskId) {
        Object result = updateHistoryTaskServiceJPA.getUpdateHistoryTaskEntitiesByTaskId(taskId);
        return ResponseUtils.getResponseEntity(result);
    }

}
