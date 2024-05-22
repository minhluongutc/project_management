package datn.backend.repositories.jpa;

import datn.backend.entities.StatusIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusIssueRepositoryJPA extends JpaRepository<StatusIssueEntity, String> {

    @Query("from StatusIssueEntity s" +
            " where s.projectId = :projectId" +
            " and (:keySearch is null or (" +
            "       lower(s.name) like lower(concat('%', :keySearch, '%')) " +
            "   or  lower(s.description) like lower(concat('%', :keySearch, '%')))" +
            "     )" +
            " and s.enabled = 1" +
            " order by s.code desc")
    List<StatusIssueEntity> getStatusIssue(String projectId, String keySearch);

    StatusIssueEntity getStatusIssueEntitiesByIdAndCodeAndEnabled(String id, Integer code, Integer enabled);
}
