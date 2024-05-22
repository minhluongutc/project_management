package datn.backend.service.impl;

import datn.backend.dto.ProjectDTO;
import datn.backend.dto.TaskDTO;
import datn.backend.dto.TreeDTO;
import datn.backend.entities.ProjectEntity;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.StatusIssueEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import datn.backend.repositories.jpa.StatusIssueRepositoryJPA;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.service.ProjectService;
import datn.backend.service.ProjectUserService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectServiceImpl implements ProjectService {
    final ProjectRepositoryJPA projectRepositoryJPA;
    final ProjectUserRepositoryJPA projectUserRepositoryJPA;
    final StatusIssueRepositoryJPA statusIssueRepositoryJPA;

    final ProjectUserService projectUserService;

    final ModelMapper modelMapper;
    private final TaskRepositoryJPA taskRepositoryJPA;

    public List<TreeDTO> getProjectsByUserId(String userId) {
        List<ProjectEntity> projectParents = projectRepositoryJPA.getProjectParentByUserId(userId);
        List<TreeDTO> trees = new ArrayList<>();
        for (ProjectEntity projectParent : projectParents) {
            TreeDTO treeDTO = new TreeDTO();

            TaskDTO.TaskQueryDTO taskQueryDTOByProjectId = new TaskDTO.TaskQueryDTO();
            taskQueryDTOByProjectId.setProjectId(projectParent.getId());
            List<TaskDTO. TaskResponseDTO> listTaskByStatusAll = taskRepositoryJPA.getTasks(taskQueryDTOByProjectId);

            ProjectDTO.ProjectResponseDTO projectResponseDTO = new ProjectDTO.ProjectResponseDTO();
            List<StatusIssueEntity> statusIssueEntities = statusIssueRepositoryJPA.getStatusIssue(projectParent.getId(), null);
            List<ProjectDTO.StatusDTO> statusPercents = new ArrayList<>();
            if (!statusIssueEntities.isEmpty()) {
                for (StatusIssueEntity statusIssueEntity : statusIssueEntities) {
                    // get tasks by status issue
                    TaskDTO.TaskQueryDTO taskQueryDTO = new TaskDTO.TaskQueryDTO();
                    taskQueryDTO.setStatusIssueId(List.of(statusIssueEntity.getId()));
                    List<TaskDTO. TaskResponseDTO> listTaskByStatus = taskRepositoryJPA.getTasks(taskQueryDTO);

                    ProjectDTO.StatusDTO status = new ProjectDTO.StatusDTO();
                    status.setName(statusIssueEntity.getName());
                    if (!listTaskByStatus.isEmpty()) {
                        status.setPercent((((double) listTaskByStatus.size() / listTaskByStatusAll.size()) * 100));
                        String fraction = listTaskByStatus.size() + "/" + listTaskByStatusAll.size();
                        status.setFraction(fraction);
                    }
                    statusPercents.add(status);
                }
            }

            modelMapper.map(projectParent, projectResponseDTO);
            projectResponseDTO.setStatusPercents(statusPercents);
            treeDTO.setData(projectResponseDTO);
            treeDTO.setChildren(new ArrayList<>());
            setProjectChildren(treeDTO, projectResponseDTO);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setProjectChildren(TreeDTO parentTree, ProjectDTO.ProjectResponseDTO dto) {
        List<ProjectEntity> projectChildren = projectRepositoryJPA.getProjectEntitiesByParentIdAndEnabled(dto.getId(), Constants.STATUS.ACTIVE.value);
        parentTree.setChildren(new ArrayList<>());
        if (projectChildren.isEmpty()) return;
        for (ProjectEntity projectChild : projectChildren) {
            TreeDTO treeDTO = new TreeDTO();

            TaskDTO.TaskQueryDTO taskQueryDTOByProjectId = new TaskDTO.TaskQueryDTO();
            taskQueryDTOByProjectId.setProjectId(projectChild.getId());
            List<TaskDTO. TaskResponseDTO> listTaskByStatusAll = taskRepositoryJPA.getTasks(taskQueryDTOByProjectId);

            ProjectDTO.ProjectResponseDTO projectResponseDTO = new ProjectDTO.ProjectResponseDTO();
            List<StatusIssueEntity> statusIssueEntities = statusIssueRepositoryJPA.getStatusIssue(projectChild.getId(), null);
            List<ProjectDTO.StatusDTO> statusPercents = new ArrayList<>();
            if (!statusIssueEntities.isEmpty()) {
                for (StatusIssueEntity statusIssueEntity : statusIssueEntities) {
                    // get tasks by status issue
                    TaskDTO.TaskQueryDTO taskQueryDTO = new TaskDTO.TaskQueryDTO();
                    taskQueryDTO.setStatusIssueId(List.of(statusIssueEntity.getId()));
                    List<TaskDTO. TaskResponseDTO> listTaskByStatus = taskRepositoryJPA.getTasks(taskQueryDTO);

                    ProjectDTO.StatusDTO status = new ProjectDTO.StatusDTO();
                    status.setName(statusIssueEntity.getName());
                    if (!listTaskByStatus.isEmpty()) {
                        status.setPercent((((double) listTaskByStatus.size() / listTaskByStatusAll.size()) * 100));
                        String fraction = listTaskByStatus.size() + "/" + listTaskByStatusAll.size();
                        status.setFraction(fraction);
                    }
                    statusPercents.add(status);
                }
            }

            modelMapper.map(projectChild, projectResponseDTO);
            projectResponseDTO.setStatusPercents(statusPercents);

            treeDTO.setData(projectResponseDTO);
            setProjectChildren(treeDTO, projectResponseDTO);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    @Transactional
    public Object insertProject(Authentication authentication, ProjectDTO.ProjectInsertDTO dto) {
        ProjectEntity projectEntity = modelMapper.map(dto, ProjectEntity.class);
        projectEntity.setCode(generateProjectCodeUnique());
        projectEntity.setId(AuditUtils.generateUUID());
        projectEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        projectEntity.setCreateTime(AuditUtils.createTime());
        projectEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        projectRepositoryJPA.save(projectEntity);

        this.cloneBaseDataProject(projectEntity.getId());

        // add user in parent project to current project
        if (dto.getParentId() != null) {
            List<ProjectUserEntity> projectUserEntities = projectUserRepositoryJPA.getProjectUserEntitiesByProjectId(dto.getParentId());
            for (ProjectUserEntity projectUser : projectUserEntities) {
                projectUserService.addUserToProject(authentication, projectUser.getUserId(), projectEntity.getId(), projectUser.getProfessionalLevel(), projectUser.getPermission());
            }
        } else {
            projectUserService.addUserToProject(authentication, AuditUtils.createUserId(authentication), projectEntity.getId(), Constants.PROFESSIONAL_LEVEL.INTERN.value, Constants.PERMISSION.MEMBER.value);
        }
        return "Create project success!";
    }

    @Override
    public Object getProjectById(String id) {
        return projectRepositoryJPA.getProjectById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public Object updateProject(Authentication authentication, String id, ProjectDTO.ProjectUpdateDTO dto) {
        ProjectEntity projectEntity = projectRepositoryJPA.findByIdAndEnabled(id, Constants.STATUS.ACTIVE.value).orElseThrow(() -> new RuntimeException("Project not found"));
        projectEntity.setName(dto.getName());
        projectEntity.setDescription(dto.getDescription());
        projectEntity.setWarningTime(dto.getWarningTime());
        projectEntity.setDangerTime(dto.getDangerTime());
        projectEntity.setUpdateTime(AuditUtils.updateTime());
        projectEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        projectRepositoryJPA.save(projectEntity);
        return projectEntity;
    }

    private String generateProjectCodeUnique() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int randomNumberOfChar;
        char randomChar;
        for (int i = 0; i < 3; i++) {
            randomNumberOfChar = random.nextInt(26);
            randomChar = (char) ('A' + randomNumberOfChar);
            stringBuilder.append(randomChar);
        }
        List<String> projectCodes = projectRepositoryJPA.findAllByEnabled(Constants.STATUS.ACTIVE.value).stream().map(ProjectEntity::getCode).toList();
        if (projectCodes.contains(stringBuilder.toString())) {
            generateProjectCodeUnique();
        }
        return stringBuilder.toString();
    }

    private void cloneBaseDataProject(String projectId) {
        // create status issue
        createStatusIssue(projectId, Constants.STATUS_ISSUE.NEW.name, Constants.STATUS_ISSUE.NEW.value, 0);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.CONFIRMED.name, Constants.STATUS_ISSUE.CONFIRMED.value, 30);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.DEPLOY_WAITING.name, Constants.STATUS_ISSUE.DEPLOY_WAITING.value, 70);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.RESOLVE.name, Constants.STATUS_ISSUE.RESOLVE.value, 80);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.REOPEN.name, Constants.STATUS_ISSUE.REOPEN.value, 30);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.DONE.name, Constants.STATUS_ISSUE.DONE.value, 100);
        createStatusIssue(projectId, Constants.STATUS_ISSUE.REJECT.name, Constants.STATUS_ISSUE.REJECT.value, 0);
    }

    private void createStatusIssue(String projectId, String name, Integer code, Integer progress) {
        StatusIssueEntity statusIssueEntity = new StatusIssueEntity();
        statusIssueEntity.setId(AuditUtils.generateUUID());
        statusIssueEntity.setProjectId(projectId);
        statusIssueEntity.setName(name);
        statusIssueEntity.setCode(code);
        statusIssueEntity.setProgress(progress);
        statusIssueEntity.setCreateTime(AuditUtils.createTime());
        statusIssueEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        statusIssueRepositoryJPA.save(statusIssueEntity);
    }

}
