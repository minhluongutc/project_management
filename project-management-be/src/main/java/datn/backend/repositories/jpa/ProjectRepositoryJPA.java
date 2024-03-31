package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepositoryJPA extends JpaRepository<ProjectEntity, Integer> {

    @Query("select p " +
            "from ProjectEntity p join ProjectUserEntity pu on p.id = pu.projectId " +
            "where pu.userId = :userId and p.parentId is null and p.enabled = 1")
    List<ProjectEntity> getProjectParentByUserId(Integer userId);

    List<ProjectEntity> getProjectEntitiesByParentIdAndEnabled(Integer parentId, Integer enabled);

}
