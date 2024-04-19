package datn.backend.repositories.jpa;

import datn.backend.entities.StatusIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusIssueRepositoryJPA extends JpaRepository<StatusIssueEntity, String> {

    List<StatusIssueEntity> getStatusIssueEntitiesByProjectIdAndEnabledOrderByCodeAsc(String projectId, Integer enabled);
}
