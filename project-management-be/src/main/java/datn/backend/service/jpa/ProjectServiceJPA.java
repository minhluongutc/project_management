package datn.backend.service.jpa;

import datn.backend.entities.ProjectEntity;
import datn.backend.repositories.jpa.ProjectRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceJPA {
    private final ProjectRepositoryJPA projectRepositoryJPA;

    public List<ProjectEntity> getAllProjects() {
        return projectRepositoryJPA.findAll();
    }

    public List<ProjectEntity> getProjectsByUserId(Integer userId) {
        return projectRepositoryJPA.getProjectEntitiesByUserId(userId);
    }
}
