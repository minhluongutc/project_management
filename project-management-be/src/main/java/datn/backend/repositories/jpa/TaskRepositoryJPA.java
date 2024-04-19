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
            " where t.projectId = :#{#dto.projectId}" +
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

    List<TaskEntity> getTaskEntitiesByParentIdAndEnabled(String parentId, Integer enabled);

    @Query("select new datn.backend.dto.TaskDTO$TaskResponseDTO(t.id, t.taskCode, p.name, t.subject, t.description, t2.name, s.name, t.priority, t.severity, t3.subject, u.username, u2.username, c.name, t.startDate, t.dueDate, t.createTime, t.updateTime, u3.username, u4.username, t.isPublic, t.projectId) " +
            " from TaskEntity t" +
            " left join ProjectEntity p on t.projectId = p.id" +
            " join ProjectUserEntity p2 on p.id = p2.projectId" +
            " left join TypeEntity t2 on t.typeId = t2.id" +
            " left join StatusIssueEntity s on t.statusIssueId = s.id" +
            " left join TaskEntity t3 on t.parentId = t3.id" +
            " left join UserEntity u on t.assignUserId = u.id" +
            " left join UserEntity u2 on t.reviewUserId = u.id" +
            " left join UserEntity u3 on t.createUserId = u.id" +
            " left join UserEntity u4 on t.updateUserId = u.id" +
            " left join CategoryEntity c on t.categoryId = c.id" +
            " where ((:parentId is null and t.parentId is null) or t.parentId = :parentId)" +
            " and (p2.userId = :userId)" +
            " and (:#{#dto.projectId} is null or t.projectId = :#{#dto.projectId})" +
            " and (" +
            "       :#{#dto.statusIssueCode} is null " +
            "       or (:#{#dto.statusIssueCodeIsEqual} = true and s.code = :#{#dto.statusIssueCode}) " +
            "       or (:#{#dto.statusIssueCodeIsEqual} = false and s.code != :#{#dto.statusIssueCode})" +
            "   )" +
            " and (t.enabled = :enabled)")
    List<TaskDTO.TaskResponseDTO> getTaskEntitiesByParentIdAndProjectIdAndEnabled(TaskDTO.TaskQueryDTO dto, String parentId, String userId, Integer enabled);
}
