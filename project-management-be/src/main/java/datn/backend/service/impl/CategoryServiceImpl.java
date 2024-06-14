package datn.backend.service.impl;

import datn.backend.dto.CategoryDTO;
import datn.backend.entities.CategoryEntity;
import datn.backend.entities.TaskEntity;
import datn.backend.repositories.jpa.CategoryRepositoryJPA;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.service.CategoryService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.ErrorApp;
import datn.backend.utils.exceptions.CustomException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepositoryJPA categoryRepositoryJPA;
    final TaskRepositoryJPA taskRepositoryJPA;

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

        List<TaskEntity> taskInUse = taskRepositoryJPA.getTaskEntitiesByCategoryIdAndEnabledAndIsPublic(id, AuditUtils.enable(), true);
        if (!taskInUse.isEmpty()) {
            return ErrorApp.CATEGORY_IN_USE;
        }

        categoryEntity.setEnabled(AuditUtils.disable());
        categoryEntity.setUpdateTime(AuditUtils.updateTime());
        categoryEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        categoryRepositoryJPA.save(categoryEntity);
        return categoryEntity;
    }
}
