package datn.backend.service.impl;

import datn.backend.dto.TaskDTO;
import datn.backend.dto.TreeDTO;
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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskServiceImpl implements TaskService {
    final TaskRepositoryJPA taskRepositoryJPA;
    final DocumentService documentService;
    final ModelMapper modelMapper;


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

    @Override
    public Object getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskEntities = taskRepositoryJPA.getTaskEntitiesByParentIdAndProjectIdAndEnabled(dto, null, AuditUtils.getUserId(authentication), Constants.STATUS.ACTIVE.value);
        System.out.println(dto);
        List<TreeDTO> trees = new ArrayList<>();
        for (TaskDTO.TaskResponseDTO taskEntity : taskEntities) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskEntity);
            treeDTO.setChildren(new ArrayList<>());
            setTaskChildren(authentication, treeDTO, taskEntity, dto);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setTaskChildren(Authentication authentication, TreeDTO parentTree, TaskDTO.TaskResponseDTO taskDTO, TaskDTO.TaskQueryDTO dto) {
//        queryDTO.setProjectId(taskDTO.getProjectId());
        List<TaskDTO.TaskResponseDTO> taskChildren = taskRepositoryJPA.getTaskEntitiesByParentIdAndProjectIdAndEnabled(dto, taskDTO.getId(), AuditUtils.getUserId(authentication), Constants.STATUS.ACTIVE.value);
        parentTree.setChildren(new ArrayList<>());
        if (taskChildren.isEmpty()) return;
        for (TaskDTO.TaskResponseDTO taskChild : taskChildren) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskChild);
            setTaskChildren(authentication, treeDTO, taskChild, dto);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    public Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files) {
        System.out.println(dto);
        // save entity
        TaskEntity taskEntity = new TaskEntity();
        taskEntity = modelMapper.map(dto, TaskEntity.class);
        taskEntity.setId(AuditUtils.generateUUID());
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
}
