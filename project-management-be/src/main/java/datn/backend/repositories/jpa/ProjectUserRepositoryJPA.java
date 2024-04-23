package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepositoryJPA extends JpaRepository<ProjectUserEntity, String> {

    List<ProjectUserEntity> getProjectUserEntitiesByProjectId(String projectId);

    Optional<ProjectUserEntity> getProjectUserEntityByUserIdAndProjectId(String userId, String projectId);
}
