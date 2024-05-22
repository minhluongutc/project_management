package datn.backend.service.impl;

import datn.backend.dto.StatusIssueDTO;
import datn.backend.entities.StatusIssueEntity;
import datn.backend.repositories.jpa.StatusIssueRepositoryJPA;
import datn.backend.service.StatusIssueService;
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
public class StatusIssueServiceImpl implements StatusIssueService {
    final StatusIssueRepositoryJPA statusIssueRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object insertStatusIssue(Authentication authentication, StatusIssueDTO.StatusIssueRequestDTO dto) {
        StatusIssueEntity statusIssueEntity = new StatusIssueEntity();
        modelMapper.map(dto, statusIssueEntity);
        statusIssueEntity.setId(AuditUtils.generateUUID());
        statusIssueEntity.setEnabled(AuditUtils.enable());
        statusIssueEntity.setCreateTime(AuditUtils.createTime());
        statusIssueEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        statusIssueRepositoryJPA.save(statusIssueEntity);
        return statusIssueEntity;
    }

    @Override
    public Object updateStatusIssue(Authentication authentication, StatusIssueDTO.StatusIssueRequestDTO dto, String id) {
        StatusIssueEntity statusIssueEntity = statusIssueRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Status Issue not found"));
        statusIssueEntity.setName(dto.getName());
        statusIssueEntity.setDescription(dto.getDescription());
        statusIssueEntity.setProjectId(dto.getProjectId());
        statusIssueEntity.setProgress(dto.getProgress());
        statusIssueEntity.setUpdateTime(AuditUtils.updateTime());
        statusIssueEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        statusIssueRepositoryJPA.save(statusIssueEntity);
        return statusIssueEntity;
    }

    @Override
    public Object deleteStatusIssue(Authentication authentication, String id) {
        StatusIssueEntity statusIssueEntity = statusIssueRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Status Issue not found"));
        statusIssueEntity.setEnabled(AuditUtils.disable());
        statusIssueEntity.setUpdateTime(AuditUtils.updateTime());
        statusIssueEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        statusIssueRepositoryJPA.save(statusIssueEntity);
        return statusIssueEntity;
    }
}
