package datn.backend.service.impl;

import datn.backend.dto.TaskDTO;
import datn.backend.entities.TaskEntity;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.service.DocumentService;
import datn.backend.service.TaskService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import datn.backend.utils.response.BaseResultSelect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskServiceImpl implements TaskService {
    final TaskRepositoryJPA taskRepositoryJPA;
    final DocumentService documentService;
    final ModelMapper modelMapper;

    @Override
    public Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files) {
        // save entity
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(AuditUtils.generateUUID());
        taskEntity = modelMapper.map(dto, TaskEntity.class);
        taskEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        taskEntity.setCreateTime(AuditUtils.createTime());
        taskEntity.setEnabled(AuditUtils.enable());
        taskRepositoryJPA.save(taskEntity);

        // save file
        if (files != null) {
            documentService.addAttachments(authentication, taskEntity.getId(), files, Constants.DOCUMENT_TYPE.TASK.value);
        }
        return "Thành công";
    }


    @Override
    public Object getTasksByProjectId(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        Pageable pageable;
        Sort sort = Sort.unsorted();
        if (dto.getSortBy() != null && dto.getSortDir() != null) {
            sort = Sort.by(dto.getSortDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, dto.getSortBy());
        }
        if (dto.getPageIndex() != null && dto.getPageSize() != null) {
            pageable = PageRequest.of(dto.getPageIndex(), dto.getPageSize(), sort);
        } else {
            pageable = Pageable.unpaged();
        }
        Page<TaskEntity> page = taskRepositoryJPA.getTaskEntityByProjectId(dto, pageable);
        return new BaseResultSelect(page.getContent(), page.getTotalElements());
    }
}
