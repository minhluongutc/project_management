package datn.backend.service.impl;

import datn.backend.dto.TypeDTO;
import datn.backend.entities.TaskEntity;
import datn.backend.entities.TypeEntity;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.repositories.jpa.TypeRepositoryJPA;
import datn.backend.service.TypeService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import datn.backend.utils.ErrorApp;
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
public class TypeServiceImpl implements TypeService {
    final TypeRepositoryJPA typeRepositoryJPA;
    final TaskRepositoryJPA taskRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object insertType(Authentication authentication, TypeDTO.TypeRequestDTO dto) {
        TypeEntity typeEntity = new TypeEntity();
        modelMapper.map(dto, typeEntity);
        typeEntity.setId(AuditUtils.generateUUID());
        typeEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        typeEntity.setCreateTime(AuditUtils.createTime());
        typeEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        typeRepositoryJPA.save(typeEntity);
        return typeEntity;
    }

    @Override
    public Object updateType(Authentication authentication, TypeDTO.TypeRequestDTO dto, String id) {
        TypeEntity typeEntity = typeRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Type not found"));
        modelMapper.map(dto, typeEntity);
        typeEntity.setId(id);
        typeEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        typeEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        typeEntity.setUpdateTime(AuditUtils.updateTime());
        typeRepositoryJPA.save(typeEntity);
        return typeEntity;
    }

    @Override
    public Object deleteType(Authentication authentication, String id) {
        TypeEntity typeEntity = typeRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Type not found"));

        List<TaskEntity> taskInUse = taskRepositoryJPA.getTaskEntitiesByTypeIdAndEnabledAndIsPublic(id, Constants.STATUS.ACTIVE.value, true);
        if (!taskInUse.isEmpty()) {
            return ErrorApp.TYPE_IN_USE;
        }

        typeEntity.setEnabled(Constants.STATUS.IN_ACTIVE.value);
        typeEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        typeEntity.setUpdateTime(AuditUtils.updateTime());
        typeRepositoryJPA.save(typeEntity);
        return typeEntity;
    }
}
