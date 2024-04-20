package datn.backend.service.impl;

import datn.backend.dto.ProjectDTO;
import datn.backend.dto.TreeDTO;
import datn.backend.entities.ProjectEntity;
import datn.backend.entities.ProjectUserEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import datn.backend.service.ProjectService;
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
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectServiceImpl implements ProjectService {
    final ProjectRepositoryJPA projectRepositoryJPA;
    final ProjectUserRepositoryJPA projectUserRepositoryJPA;

    final ModelMapper modelMapper;

    public List<TreeDTO> getProjectsByUserId(String userId) {
        List<ProjectEntity> projectParents = projectRepositoryJPA.getProjectParentByUserId(userId);
        List<TreeDTO> trees = new ArrayList<>();
        for (ProjectEntity projectParent : projectParents) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(projectParent);
            treeDTO.setChildren(new ArrayList<>());
            setProjectChildren(treeDTO, projectParent);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setProjectChildren(TreeDTO parentTree, ProjectEntity projectEntity) {
        List<ProjectEntity> projectChildren = projectRepositoryJPA.getProjectEntitiesByParentIdAndEnabled(projectEntity.getId(), Constants.STATUS.ACTIVE.value);
        parentTree.setChildren(new ArrayList<>());
        if (projectChildren.isEmpty()) return;
        for (ProjectEntity projectChild : projectChildren) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(projectChild);
            setProjectChildren(treeDTO, projectChild);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    @Transactional
    public Object insertProject(Authentication authentication, ProjectDTO.ProjectInsertDTO dto) {
        ProjectEntity projectEntity = modelMapper.map(dto, ProjectEntity.class);
        projectEntity.setCode(generateProjectCodeUniqueEachCompany(AuditUtils.getCompanyId(authentication)));
        projectEntity.setId(AuditUtils.generateUUID());
        projectEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        projectEntity.setCreateTime(AuditUtils.createTime());
        projectEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        projectRepositoryJPA.save(projectEntity);

        ProjectUserEntity projectUserEntity = new ProjectUserEntity();
        projectUserEntity.setProjectId(projectEntity.getId());
        projectUserEntity.setUserId(AuditUtils.createUserId(authentication));
        projectUserEntity.setCreateTime(AuditUtils.createTime());
        projectUserEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        projectUserRepositoryJPA.save(projectUserEntity);
        return "ok";
    }

    private String generateProjectCodeUniqueEachCompany(String companyId) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int randomNumberOfChar;
        char randomChar;
        for (int i = 0; i < 3; i++) {
            randomNumberOfChar = random.nextInt(26);
            randomChar = (char) ('A' + randomNumberOfChar);
            stringBuilder.append(randomChar);
        }
        List<String> projectCodes = projectRepositoryJPA.getProjectEntitiesByCompanyId(companyId).stream().map(ProjectEntity::getCode).toList();
        if (projectCodes.contains(stringBuilder.toString())) {
            generateProjectCodeUniqueEachCompany(companyId);
        }
        return stringBuilder.toString();
    }
}
