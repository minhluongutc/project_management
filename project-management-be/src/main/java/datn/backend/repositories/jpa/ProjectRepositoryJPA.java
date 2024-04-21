package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepositoryJPA extends JpaRepository<ProjectEntity, String> {

    @Query("select p " +
            "from ProjectEntity p join ProjectUserEntity pu on p.id = pu.projectId " +
            "where pu.userId = :userId and p.parentId is null and p.enabled = 1")
    List<ProjectEntity> getProjectParentByUserId(String userId);

    List<ProjectEntity> getProjectEntitiesByParentIdAndEnabled(String parentId, Integer enabled);

    List<ProjectEntity> getProjectEntitiesByCompanyId(String companyId);

    @Query("select p.code from ProjectEntity p where p.id = :id")
    String getProjectCodeById(String id);

    @Query("select p " +
            "from ProjectEntity p " +
            "where p.id = :id and p.enabled = 1")
    Optional<ProjectEntity> getProjectById(String id);

}
