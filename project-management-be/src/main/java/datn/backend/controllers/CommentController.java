package datn.backend.controllers;

import datn.backend.service.CommentService;
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
public class CommentController {
    final CommentService commentService;

    @GetMapping(value = "{objectId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCommentsByProjectId(@PathVariable("objectId") String objectId) {
        Object result = commentService.getCommentsByObjectId(objectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "{objectId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertComment(Authentication authentication,
                                                @PathVariable("objectId") String objectId,
                                                @RequestBody String content) {
        Object result = commentService.insertComment(authentication, content, objectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "{objectId}/comments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateComment(Authentication authentication,
                                                @PathVariable("objectId") String objectId,
                                                @PathVariable("id") String id,
                                                @RequestBody String content) {
        Object result = commentService.updateComment(authentication, content, objectId, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "{objectId}/comments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteComment(Authentication authentication,
                                                @PathVariable("objectId") String objectId,
                                                @PathVariable("id") String id) {
        Object result = commentService.deleteComment(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
