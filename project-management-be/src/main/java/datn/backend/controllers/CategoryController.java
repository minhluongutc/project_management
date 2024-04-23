package datn.backend.controllers;

import datn.backend.dto.CategoryDTO;
import datn.backend.service.CategoryService;
import datn.backend.service.jpa.CategoryServiceJPA;
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
public class CategoryController {
    final CategoryServiceJPA categoryServiceJPA;
    final CategoryService categoryService;

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCategoriesByProjectId(String projectId, String keySearch) {
        Object result = categoryServiceJPA.getCategoryEntitiesByProjectId(projectId, keySearch);
        return ResponseUtils.getResponseEntity(result);
    }

    @PostMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertCategory(Authentication authentication, @RequestBody CategoryDTO.CategoryRequestDTO dto) {
        Object result = categoryService.insertCategory(authentication, dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateCategory(Authentication authentication,
                                                 @RequestBody CategoryDTO.CategoryRequestDTO dto,
                                                 @PathVariable("id") String id) {
        Object result = categoryService.updateCategory(authentication, dto, id);
        return ResponseUtils.getResponseEntity(result);
    }

    @DeleteMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteCategory(Authentication authentication, @PathVariable("id") String id) {
        Object result = categoryService.deleteCategory(authentication, id);
        return ResponseUtils.getResponseEntity(result);
    }
}
