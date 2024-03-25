package datn.backend.service.impl;

import datn.backend.dto.ProjectDTO;
import datn.backend.entities.ProjectEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import datn.backend.service.ProjectService;
import datn.backend.utils.AuditUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectServiceImpl implements ProjectService {
    final ProjectRepositoryJPA projectRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object insertProject(Authentication authentication, ProjectDTO.ProjectInsertDTO dto) {
        ProjectEntity entity = modelMapper.map(dto, ProjectEntity.class);
        entity.setCreateUserId(AuditUtils.createUserId(authentication));
        entity.setCreateTime(AuditUtils.createTime());
        projectRepositoryJPA.save(entity);
        return "ok";
    }
}
