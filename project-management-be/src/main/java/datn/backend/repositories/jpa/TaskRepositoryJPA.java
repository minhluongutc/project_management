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
import java.util.Optional;

@Repository
public interface TaskRepositoryJPA extends JpaRepository<TaskEntity, String> {

    @Query("select t " +
            " from TaskEntity t" +
            " where t.projectId = :#{#dto.projectId}" +
            " and t.enabled = 1" +
            " and (:#{#dto.subject} is null or upper(t.subject) like upper(CAST(trim(:#{#dto.subject}) as String)))" +
            " and (:#{#dto.description} is null or upper(t.description) like upper(CAST(trim(:#{#dto.description}) as String)))" +
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

    @Query("select new datn.backend.dto.TaskDTO$TaskResponseDTO(" +
            "t.id, t.taskCode, p.name, t.subject, t.description, t2.name," +
            " s.name, t.priority, t.severity, t3.subject, u.username," +
            " u2.username, c.name, t.startDate, t.dueDate, t.createTime," +
            " t.updateTime, u3.username, u4.username, t.isPublic, t.projectId) " +
            " from TaskEntity t" +
            " left join ProjectEntity p on t.projectId = p.id" +
            " join ProjectUserEntity p2 on (p.id = p2.projectId and :userId = p2.userId)" +
            " left join TypeEntity t2 on t.typeId = t2.id" +
            " left join StatusIssueEntity s on t.statusIssueId = s.id" +
            " left join TaskEntity t3 on t.parentId = t3.id" +
            " left join UserEntity u on t.assignUserId = u.id" +
            " left join UserEntity u2 on t.reviewUserId = u2.id" +
            " left join UserEntity u3 on t.createUserId = u3.id" +
            " left join UserEntity u4 on t.updateUserId = u4.id" +
            " left join CategoryEntity c on t.categoryId = c.id" +
            " where (:parentId is null or t.parentId = :parentId)" +
            " and (:#{#dto.projectId} is null or t.projectId = :#{#dto.projectId})" +
            " and (:#{#dto.assignUserId} is null or (t.assignUserId = :#{#dto.assignUserId}))" +
            " and (:#{#dto.createUserId} is null or (t.createUserId = :#{#dto.createUserId}))" +
            " and (t.enabled = 1)")
    List<TaskDTO.TaskResponseDTO> getTasksLevel(TaskDTO.TaskQueryDTO dto, String parentId, String userId);

    @Query("select new datn.backend.dto.TaskDTO$TaskResponseGetChildren(t.id, t.taskCode, t.projectId, t.subject, t.description, t.isPublic, t.typeId, t.statusIssueId, t.priority, t.severity, t.parentId, t.assignUserId, t.reviewUserId, t.categoryId, t.startDate, t.dueDate, s.progress)" +
            " from TaskEntity t " +
            " left join StatusIssueEntity s on t.statusIssueId = s.id" +
            " where t.parentId = :parentId and t.enabled = 1")
    List<TaskDTO.TaskResponseGetChildren> getTaskEntitiesByParentIdAndEnabled(String parentId);

    @Query("select new datn.backend.dto.TaskDTO$TaskResponseDTO(" +
            " t.id, t.taskCode, p.name, t.subject, t.description, t2.name, s.name, " +
            " t.priority, t.severity, t3.subject, u.username, u2.username, c.name, " +
            " t.startDate, t.dueDate, t.createTime, t.updateTime, u3.username, u4.username, " +
            " t.isPublic, t.projectId, t3.taskCode, t3.subject, t.parentId, s.progress, " +
            " p.warningTime, p.dangerTime) " +
            " from TaskEntity t" +
            " left join ProjectEntity p on t.projectId = p.id" +
            " left join TypeEntity t2 on t.typeId = t2.id" +
            " left join StatusIssueEntity s on t.statusIssueId = s.id" +
            " left join TaskEntity t3 on t.parentId = t3.id" +
            " left join UserEntity u on t.assignUserId = u.id" +
            " left join UserEntity u2 on t.reviewUserId = u2.id" +
            " left join UserEntity u3 on t.createUserId = u3.id" +
            " left join UserEntity u4 on t.updateUserId = u4.id" +
            " left join CategoryEntity c on t.categoryId = c.id" +
            " where (:#{#dto.parentId} is null or (t.parentId = :#{#dto.parentId}))" +
            " and (:#{#dto.projectId} is null or :#{#dto.projectId} = '' or (t.projectId = :#{#dto.projectId}))" +
            " and (" +
            "       coalesce(:#{#dto.typeId}, null) is null " +
            "       or (:#{#dto.isTypeIdEmpty} = true)" +
            "       or (:#{#dto.typeIdIsEqual} = true and t.typeId in :#{#dto.typeId})" +
            "       or (:#{#dto.typeIdIsEqual} = false and t.typeId not in :#{#dto.typeId})" +
            "   )" +
            " and (" +
            "       coalesce(:#{#dto.priority}, null) is null " +
            "       or (:#{#dto.isPriorityEmpty} = true) " +
            "       or (:#{#dto.priorityIsEqual} = true and t.priority in :#{#dto.priority})" +
            "       or (:#{#dto.priorityIsEqual} = false and t.priority not in :#{#dto.priority})" +
            "   )" +
            " and (" +
            "       coalesce(:#{#dto.severity}, null) is null " +
            "       or (:#{#dto.isSeverityEmpty} = true)" +
            "       or (:#{#dto.severityIsEqual} = true and t.severity in :#{#dto.severity})" +
            "       or (:#{#dto.severityIsEqual} = false and t.severity not in :#{#dto.severity})" +
            "   )" +
            " and (" +
            "       coalesce(:#{#dto.assignUserId}, null) is null " +
            "       or (:#{#dto.isAssignUserIdEmpty()} = true)" +
            "       or (:#{#dto.assignUserIdIsEqual} = true and t.assignUserId in :#{#dto.assignUserId})" +
            "       or (:#{#dto.assignUserIdIsEqual} = false and t.assignUserId not in :#{#dto.assignUserId})" +
            "   )" +
            " and (:#{#dto.createUserId} is null or (t.createUserId = :#{#dto.createUserId}))" +
            " and (" +
            "       coalesce(:#{#dto.statusIssueId}, null) is null" +
            "       or (:#{#dto.isStatusIssueEmpty} = true)" +
            "       or (:#{#dto.statusIssueIsEqual} = true and t.statusIssueId in :#{#dto.statusIssueId})" +
            "       or (:#{#dto.statusIssueIsEqual} = false and t.statusIssueId not in :#{#dto.statusIssueId})" +
            "   )" +
            " and (" +
            "       coalesce(:#{#dto.categoryId}, null) is null " +
            "       or (:#{#dto.isCategoryEmpty} = true)" +
            "       or (:#{#dto.categoryIdIsEqual} = true and t.categoryId in :#{#dto.categoryId})" +
            "       or (:#{#dto.categoryIdIsEqual} = false and t.categoryId not in :#{#dto.categoryId})" +
            "   )" +
            " and (" +
            "       coalesce(:#{#dto.reviewUserId}, null) is null " +
            "       or (:#{#dto.isReviewUserIdEmpty} = true)" +
            "       or (:#{#dto.reviewUserIdIsEqual} = true and t.reviewUserId in :#{#dto.reviewUserId})" +
            "       or (:#{#dto.reviewUserIdIsEqual} = false and t.reviewUserId not in :#{#dto.reviewUserId})" +
            "   )" +
            " and (" +
            "       :#{#dto.startDate} is null " +
            "       or (:#{#dto.startDateOperator} = 'bang' and t.startDate = :#{#dto.startDate})" +
            "       or (:#{#dto.startDateOperator} = 'khac' and t.startDate != :#{#dto.startDate})" +
            "       or (:#{#dto.startDateOperator} = 'lon' and t.startDate > :#{#dto.startDate})" +
            "       or (:#{#dto.startDateOperator} = 'lonBang' and t.startDate >= :#{#dto.startDate})" +
            "       or (:#{#dto.startDateOperator} = 'nho' and t.startDate < :#{#dto.startDate})" +
            "       or (:#{#dto.startDateOperator} = 'nhoBang' and t.startDate <= :#{#dto.startDate})" +
            "   )" +
            " and (" +
            "       :#{#dto.endDate} is null " +
            "       or (:#{#dto.endDateOperator} = 'bang' and t.dueDate = :#{#dto.endDate})" +
            "       or (:#{#dto.endDateOperator} = 'khac' and t.dueDate != :#{#dto.endDate})" +
            "       or (:#{#dto.endDateOperator} = 'lon' and t.dueDate > :#{#dto.endDate})" +
            "       or (:#{#dto.endDateOperator} = 'lonBang' and t.dueDate >= :#{#dto.endDate})" +
            "       or (:#{#dto.endDateOperator} = 'nho' and t.dueDate < :#{#dto.endDate})" +
            "       or (:#{#dto.endDateOperator} = 'nhoBang' and t.dueDate <= :#{#dto.endDate})" +
            "   )" +
            " and (:#{#dto.keyword} is null or :#{#dto.keyword} = '' or (lower(t.subject) like lower(CAST(trim(:#{#dto.keyword}) as String))))" +
            " and (t.enabled = 1)" +
            " order by t.createTime desc")
    List<TaskDTO.TaskResponseDTO> getTasks(@Param("dto") TaskDTO.TaskQueryDTO dto);

    @Query("select new datn.backend.dto.TaskDTO$TaskDetailResponseDTO(t.id, t.taskCode, t.projectId, p.name, t.subject, t.description, t.isPublic, t.typeId, t.statusIssueId, t.priority, t.severity, t.parentId, t2.subject, t.assignUserId, t.reviewUserId, t.categoryId, t.startDate, t.dueDate, t.estimateTime, t.createUserId, u.username, t.createTime, t.updateUserId, t.updateTime, t.enabled)" +
            " from TaskEntity t " +
            " join ProjectEntity p on t.projectId = p.id" +
            " left join TaskEntity t2 on t.parentId = t2.id" +
            " join UserEntity u on t.createUserId = u.id" +
            " where t.id = :id")
    Optional<TaskDTO.TaskDetailResponseDTO> getTaskDetail(String id);

    List<TaskEntity> getTaskEntitiesByProjectId(String projectId);

    @Query("select t from TaskEntity t where t.parentId = :parentId")
    List<TaskEntity> getChildrenTaskByParentId(String parentId);

    List<TaskEntity> getTaskEntitiesByProjectIdAndEnabled(String projectId, Integer enabled);

    List<TaskEntity> getTaskEntitiesByProjectIdAndAssignUserIdAndEnabled(String projectId, String userId, Integer enabled);

    @Query("select t from TaskEntity t " +
            " where :projectId is null or :projectId = '' or t.projectId = :projectId " +
            " and t.assignUserId = :userId" +
            " and t.enabled = 1")
    List<TaskEntity> getTaskEntitiesByProjectIdAndAssignUserId(String projectId, String userId);

    List<TaskEntity> getTaskEntitiesByCategoryIdAndEnabledAndIsPublic(String categoryId, Integer enabled, boolean isPublic);
    List<TaskEntity> getTaskEntitiesByStatusIssueIdAndEnabledAndIsPublic(String statusIssueId, Integer enabled, boolean isPublic);
    List<TaskEntity> getTaskEntitiesByTypeIdAndEnabledAndIsPublic(String typeId, Integer enabled, boolean isPublic);

    TaskEntity findFirstById(String id);
}
