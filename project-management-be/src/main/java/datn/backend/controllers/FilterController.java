package datn.backend.controllers;

import datn.backend.dto.FilterInsertDTO;
import datn.backend.service.FilterService;
import datn.backend.service.jpa.FilterServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import jakarta.validation.Valid;
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
public class FilterController {
    final FilterServiceJPA filterServiceJPA;

    final FilterService filterService;

    @GetMapping(value = "filters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFilter(Authentication authentication, String projectId) {
        Object result = filterServiceJPA.findByUserIdAndProjectIdAndEnabled(authentication, projectId);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "filters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertFilter(Authentication authentication,
                                               @Valid @RequestBody FilterInsertDTO dto) {
        Object result = filterService.insertFilter(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "filters/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateFilterName(Authentication authentication,
                                                   @PathVariable String id,
                                                   @RequestBody(required = true) String filterName) {
        Object result = filterService.updateFilterName(id, filterName);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "filters/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteFilter(@PathVariable String id) {
        Object result = filterService.deleteFilter(id);
        return ResponseUtils.getResponseEntity(result);
    }
}
