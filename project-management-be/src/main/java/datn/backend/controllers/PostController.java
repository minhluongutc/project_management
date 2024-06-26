package datn.backend.controllers;

import datn.backend.dto.PostInsertDTO;
import datn.backend.service.PostService;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostController {
    final PostService postService;

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPosts(Authentication authentication, String projectId) {
        Object result = postService.getPosts(authentication, projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> insertPost(Authentication authentication,
                                             @RequestPart("dto") PostInsertDTO dto,
                                             @RequestPart(value = "files", required = false) MultipartFile[] files) {
        Object result = postService.insertPost(authentication, dto, files);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/posts/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> updatePost(Authentication authentication,
                                             @PathVariable String id,
                                             @RequestPart("dto") PostInsertDTO dto) {
        Object result = postService.updatePost(authentication, id, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletePost(Authentication authentication, @PathVariable String id) {
        Object result = postService.deletePost(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPost(Authentication authentication, @PathVariable String id) {
        Object result = postService.getPost(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
