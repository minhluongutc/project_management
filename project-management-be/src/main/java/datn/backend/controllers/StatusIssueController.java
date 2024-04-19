package datn.backend.controllers;

import datn.backend.service.jpa.StatusIssueJPA;
import datn.backend.service.jpa.TypeServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusIssueController {
    final StatusIssueJPA statusIssueJPA;

    @GetMapping(value = "/status-issue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTypesByProjectId(String projectId) {
        Object result = statusIssueJPA.getStatusIssueEntitiesByProjectId(projectId);
        return ResponseUtils.getResponseEntity(result);
    }
}
