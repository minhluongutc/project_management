package datn.backend.repositories.jpa;

import datn.backend.dto.TaskDTO;
import datn.backend.entities.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepositoryJPA extends JpaRepository<TaskEntity, String> {

    @Query("select t " +
            " from TaskEntity t" +
            " where t.projectId = :projectId" +
            " and t.enabled = 1" +
            " and (:#{#dto.subject} is null or upper(t.subject) like upper(concat('%', trim(:#{#dto.subject}) ,'%')))" +
            " and (:#{#dto.description} is null or upper(t.description) like upper(concat('%', trim(:#{#dto.description}) ,'%')))" +
            " and (:#{#dto.isPublic} is null or t.isPublic = :#{#dto.isPublic})" +
            " and (:#{#dto.typeId} is null or t.typeId = :#{#dto.typeId})" +
            " and (:#{#dto.statusIssueId} is null or t.statusIssueId = :#{#dto.statusIssueId})" +
            " and (:#{#dto.priority} is null or t.priority = :#{#dto.priority})" +
            " and (:#{#dto.severity} is null or t.severity = :#{#dto.severity})" +
            " and (:#{#dto.parentId} is null or t.parentId = :#{#dto.parentId})" +
            " and (:#{#dto.assignUserId} is null or t.assignUserId = :#{#dto.assignUserId})" +
            " and (:#{#dto.reviewUserId} is null or t.reviewUserId = :#{#dto.reviewUserId})" +
            " and (:#{#dto.date} is null or date(:#{#dto.date}) >= date(t.startDate) and date(:#{#dto.date}) <= date(t.dueDate))")
    Page<TaskEntity> getTaskEntityByProjectId(@Param("dto") TaskDTO.TaskQueryDTO dto, Pageable pageable);
}
