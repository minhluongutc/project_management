package datn.backend.service.impl;

import datn.backend.dto.StatusIssueDTO;
import datn.backend.entities.StatusIssueEntity;
import datn.backend.entities.TaskEntity;
import datn.backend.repositories.jpa.StatusIssueRepositoryJPA;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.service.StatusIssueService;
import datn.backend.utils.AuditUtils;
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
public class StatusIssueServiceImpl implements StatusIssueService {
    final StatusIssueRepositoryJPA statusIssueRepositoryJPA;
    final TaskRepositoryJPA taskRepositoryJPA;

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

        List<TaskEntity> taskInUse = taskRepositoryJPA.getTaskEntitiesByStatusIssueIdAndEnabledAndIsPublic(id, AuditUtils.enable(), true);
        if (!taskInUse.isEmpty()) {
            return ErrorApp.STATUS_ISSUE_IN_USE;
        }

        statusIssueEntity.setEnabled(AuditUtils.disable());
        statusIssueEntity.setUpdateTime(AuditUtils.updateTime());
        statusIssueEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        statusIssueRepositoryJPA.save(statusIssueEntity);
        return statusIssueEntity;
    }
}
