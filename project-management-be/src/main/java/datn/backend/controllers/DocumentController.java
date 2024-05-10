package datn.backend.controllers;

import datn.backend.dto.UploadFileDTO;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentController {
    final DocumentService documentService;
    final DocumentServiceJPA documentServiceJPA;

    @GetMapping(value = "/attachments/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAttachmentsByObjectId(String objectId, Integer type) {
        Object result = documentServiceJPA.getAttachmentsByObjectIdAndType(objectId, type);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/attachments", produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Object uploadAttachment(Authentication authentication, @RequestPart("file") MultipartFile file, @RequestPart("dto") UploadFileDTO dto) {
        Object result = documentService.addAttachment(authentication, dto.getObjectId(), file, dto.getType());
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/attachments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object deleteAttachment(Authentication authentication, @PathVariable String id) {
        Object result = documentService.deleteAttachment(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
