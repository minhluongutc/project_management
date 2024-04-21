package datn.backend.service.impl;

import datn.backend.dto.AttachmentDTO;
import datn.backend.dto.TaskDTO;
import datn.backend.dto.TreeDTO;
import datn.backend.entities.TaskEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
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
import java.util.Objects;
import java.util.Random;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskServiceImpl implements TaskService {
    final TaskRepositoryJPA taskRepositoryJPA;
    final ProjectRepositoryJPA projectRepositoryJPA;
    final DocumentService documentService;
    final DocumentRepositoryJPA documentRepositoryJPA;
    final ModelMapper modelMapper;


    @Override
    public Object getTasks(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskListResponse = new ArrayList<>();
        List<TaskDTO.TaskResponseDTO> taskList = taskRepositoryJPA.getTasks(dto);
        for (TaskDTO.TaskResponseDTO task : taskList) {
            List<AttachmentDTO.AttachmentResponseDTO> attachments = documentRepositoryJPA.getAttachmentsByObjectId(task.getId());
            task.setAttachments(attachments);
            taskListResponse.add(task);
        }
        return taskListResponse;
    }

    @Override
    public Object getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskEntities = taskRepositoryJPA.getTasksLevel(dto, null, AuditUtils.getUserId(authentication), Constants.STATUS.ACTIVE.value);
        if (taskEntities.stream().anyMatch(task -> Objects.equals(task.getId(), dto.getOtherTaskId()))) {
            taskEntities.removeIf(task -> Objects.equals(task.getId(), dto.getOtherTaskId()));
        }
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
        List<TaskDTO.TaskResponseDTO> taskChildren = taskRepositoryJPA.getTasksLevel(dto, taskDTO.getId(), AuditUtils.getUserId(authentication), Constants.STATUS.ACTIVE.value);
        if (taskChildren.stream().anyMatch(task -> Objects.equals(task.getId(), dto.getOtherTaskId()))) {
            taskChildren.removeIf(task -> Objects.equals(task.getId(), dto.getOtherTaskId()));
        }
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
    public Object getTasksChildrenByParentId(Authentication authentication, String parentId) {
        List<TaskEntity> taskEntities = taskRepositoryJPA.getTaskEntitiesByParentIdAndEnabled(parentId, Constants.STATUS.ACTIVE.value);
        List<TreeDTO> trees = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskEntity);
            treeDTO.setChildren(new ArrayList<>());
            setTaskChildren(authentication, treeDTO, taskEntity);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setTaskChildren(Authentication authentication, TreeDTO parentTree, TaskEntity entity) {
        List<TaskEntity> taskChildren = taskRepositoryJPA.getTaskEntitiesByParentIdAndEnabled(entity.getId(), Constants.STATUS.ACTIVE.value);
        parentTree.setChildren(new ArrayList<>());
        if (taskChildren.isEmpty()) return;
        for (TaskEntity taskChild : taskChildren) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskChild);
            setTaskChildren(authentication, treeDTO, taskChild);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    public Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files) {
        System.out.println(dto);
        // save entity
        TaskEntity taskEntity = new TaskEntity();
        taskEntity = modelMapper.map(dto, TaskEntity.class);
        taskEntity.setTaskCode(generateTaskCodeUniqueEachProject(dto.getProjectId()));
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

    @Override
    public Object updateTask(Authentication authentication, TaskDTO.TaskUpdateDTO dto, String id) {
        TaskEntity taskEntity = taskRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskEntity.setSubject(dto.getSubject());
        taskEntity.setDescription(dto.getDescription());
        taskEntity.setIsPublic(dto.getIsPublic());
        taskEntity.setTypeId(dto.getTypeId());
        taskEntity.setStatusIssueId(dto.getStatusIssueId());
        taskEntity.setPriority(dto.getPriority());
        taskEntity.setSeverity(dto.getSeverity());
        taskEntity.setParentId(dto.getParentId());
        taskEntity.setAssignUserId(dto.getAssignUserId());
        taskEntity.setReviewUserId(dto.getReviewUserId());
        taskEntity.setStartDate(dto.getStartDate());
        taskEntity.setDueDate(dto.getDueDate());
        taskEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        taskEntity.setUpdateTime(AuditUtils.updateTime());
        taskRepositoryJPA.save(taskEntity);
        return "successfully updated";
    }

    @Override
    public Object getTask(Authentication authentication, String id) {
        return taskRepositoryJPA.getTaskDetail(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public Object getTasksChildren(Authentication authentication, String parentId) {
        return taskRepositoryJPA.getChildrenTaskByParentId(parentId);
    }

    private String generateTaskCodeUniqueEachProject(String projectId) {
        Random random = new Random();
        List<String> taskCodes = taskRepositoryJPA.getTaskEntitiesByProjectId(projectId).stream().map(TaskEntity::getTaskCode).toList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(projectRepositoryJPA.getProjectCodeById(projectId)).append("-");
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10)); // Random số từ 0 đến 9
        }
        if (taskCodes.contains(stringBuilder.toString())) {
            generateTaskCodeUniqueEachProject(projectId);
        }
        return stringBuilder.toString();
    }
}
