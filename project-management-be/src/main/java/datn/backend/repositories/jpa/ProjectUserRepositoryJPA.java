package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepositoryJPA extends JpaRepository<ProjectUserEntity, String> {

    List<ProjectUserEntity> getProjectUserEntitiesByProjectId(String projectId);

    Optional<ProjectUserEntity> getProjectUserEntityByUserIdAndProjectId(String userId, String projectId);

    @Query("SELECT pu.permission FROM ProjectUserEntity pu WHERE pu.projectId = ?1 AND pu.userId = ?2")
    Integer getRoleByProjectAndUser(String projectId, String userId);
}
