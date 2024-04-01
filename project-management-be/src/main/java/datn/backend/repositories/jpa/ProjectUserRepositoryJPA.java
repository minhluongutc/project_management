package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepositoryJPA extends JpaRepository<ProjectUserEntity, Integer> {

    List<ProjectUserEntity> getProjectUserEntitiesByProjectId(Integer projectId);
}
