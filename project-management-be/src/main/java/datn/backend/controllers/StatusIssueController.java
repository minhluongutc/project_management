package datn.backend.controllers;

import datn.backend.dto.StatusIssueDTO;
import datn.backend.service.StatusIssueService;
import datn.backend.service.jpa.StatusIssueJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusIssueController {
    final StatusIssueJPA statusIssueJPA;
    final StatusIssueService statusIssueService;

    @GetMapping(value = "/status-issue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTypesByProjectId(String projectId, String keySearch) {
        Object result = statusIssueJPA.getStatusIssue(projectId, keySearch);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/status-issue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertType(Authentication authentication, @RequestBody StatusIssueDTO.StatusIssueRequestDTO dto) {
        Object result = statusIssueService.insertStatusIssue(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/status-issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateType(Authentication authentication,
                                             @RequestBody StatusIssueDTO.StatusIssueRequestDTO dto,
                                             @PathVariable("id") String id) {
        Object result = statusIssueService.updateStatusIssue(authentication, dto, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/status-issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteType(Authentication authentication, @PathVariable("id") String id) {
        Object result = statusIssueService.deleteStatusIssue(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
