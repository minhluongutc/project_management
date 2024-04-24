package datn.backend.service;

import datn.backend.dto.CategoryDTO;
import org.springframework.security.core.Authentication;

public interface CategoryService {
    Object insertCategory(Authentication authentication, CategoryDTO.CategoryRequestDTO dto);
    Object updateCategory(Authentication authentication, CategoryDTO.CategoryRequestDTO dto, String id);
    Object deleteCategory(Authentication authentication, String id);
}
