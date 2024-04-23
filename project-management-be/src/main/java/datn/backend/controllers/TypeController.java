package datn.backend.controllers;

import datn.backend.dto.TypeDTO;
import datn.backend.service.TypeService;
import datn.backend.service.jpa.TypeServiceJPA;
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
public class TypeController {
    final TypeServiceJPA typeServiceJPA;

    final TypeService typeService;

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTypes(String projectId, String keySearch) {
        Object result = typeServiceJPA.getTypes(projectId, keySearch);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertType(Authentication authentication, @RequestBody TypeDTO.TypeRequestDTO dto) {
        Object result = typeService.insertType(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/types/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateType(Authentication authentication,
                                             @RequestBody TypeDTO.TypeRequestDTO dto,
                                             @PathVariable("id") String id) {
        Object result = typeService.updateType(authentication, dto, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/types/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteType(Authentication authentication, @PathVariable("id") String id) {
        Object result = typeService.deleteType(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }

}
