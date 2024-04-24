package datn.backend.controllers;

import datn.backend.service.DocumentService;
import datn.backend.service.jpa.DocumentServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentController {
    final DocumentService documentService;
    final DocumentServiceJPA documentServiceJPA;

    @GetMapping(value = "/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAttachmentsByObjectId(String objectId) {
        Object result = documentServiceJPA.getAttachmentsByObjectId(objectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/attachments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object deleteAttachment(Authentication authentication, @PathVariable String id) {
        Object result = documentService.deleteAttachment(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
