package datn.backend.service.impl;

import datn.backend.dto.CategoryDTO;
import datn.backend.entities.CategoryEntity;
import datn.backend.repositories.jpa.CategoryRepositoryJPA;
import datn.backend.service.CategoryService;
import datn.backend.utils.AuditUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepositoryJPA categoryRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object insertCategory(Authentication authentication, CategoryDTO.CategoryRequestDTO dto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        modelMapper.map(dto, categoryEntity);
        categoryEntity.setId(AuditUtils.generateUUID());
        categoryEntity.setEnabled(AuditUtils.enable());
        categoryEntity.setCreateTime(AuditUtils.createTime());
        categoryEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        categoryRepositoryJPA.save(categoryEntity);
        return categoryEntity;
    }

    @Override
    public Object updateCategory(Authentication authentication, CategoryDTO.CategoryRequestDTO dto, String id) {
        CategoryEntity categoryEntity = categoryRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryEntity.setName(dto.getName());
        categoryEntity.setDescription(dto.getDescription());
        categoryEntity.setProjectId(dto.getProjectId());
        categoryEntity.setUpdateTime(AuditUtils.updateTime());
        categoryEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        categoryRepositoryJPA.save(categoryEntity);
        return categoryEntity;
    }

    @Override
    public Object deleteCategory(Authentication authentication, String id) {
        CategoryEntity categoryEntity = categoryRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryEntity.setEnabled(AuditUtils.disable());
        categoryEntity.setUpdateTime(AuditUtils.updateTime());
        categoryEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        categoryRepositoryJPA.save(categoryEntity);
        return categoryEntity;
    }
}
