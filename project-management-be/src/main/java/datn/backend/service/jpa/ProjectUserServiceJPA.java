package datn.backend.service.jpa;

import datn.backend.repositories.jpa.ProjectUserRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectUserServiceJPA {
    final ProjectUserRepositoryJPA projectUserRepositoryJPA;

    public Integer getRoleByProjectAndUser(String projectId, String userId) {
        return projectUserRepositoryJPA.getRoleByProjectAndUser(projectId, userId);
    }
}
