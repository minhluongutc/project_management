package datn.backend.repositories.jpa;

import datn.backend.dto.UpdateHistoryTaskDTO;
import datn.backend.entities.UpdateHistoryTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateHistoryTaskRepositoryJPA extends JpaRepository<UpdateHistoryTaskEntity, String> {

    @Query("select new datn.backend.dto.UpdateHistoryTaskDTO(" +
            "u.id, u.taskId, u.oldSubject, u.newSubject, " +
            "u.oldDescription, u.newDescription, t1.name, t2.name, " +
            "s1.name, s2.name, u.oldPriority, u.newPriority, " +
            "u.oldSeverity, u.newSeverity, t3.subject, t4.subject, " +
            "u.oldStartDate, u.newStartDate, u.oldDueDate, u.newDueDate, " +
            "u.oldEstimateTime, u.newEstimateTime, u3.username, u4.username, " +
            "u5.username, u6.username, c1.name, c2.name, " +
            "u.modifyTime, ue.username, d.id) " +
            " from UpdateHistoryTaskEntity u " +
            " join UserEntity ue on ue.id = u.modifyUserId" +
            " left join DocumentEntity d on d.objectId = ue.id and d.type = 2 and d.enabled = 1" +
            " left join TypeEntity t1 on u.oldTypeId = t1.id" +//old type
            " left join TypeEntity t2 on u.newTypeId = t2.id" +//new type
            " left join StatusIssueEntity s1 on u.oldStatusIssueId = s1.id" +
            " left join StatusIssueEntity s2 on u.newStatusIssueId = s2.id" +
            " left join TaskEntity t3 on u.oldParentId = t3.id" +
            " left join TaskEntity t4 on u.newParentId = t4.id" +
            " left join UserEntity u3 on u.oldAssignUserId = u3.id" +
            " left join UserEntity u4 on u.newAssignUserId = u4.id" +
            " left join UserEntity u5 on u.oldReviewUserId = u5.id" +
            " left join UserEntity u6 on u.newReviewUserId = u6.id" +
            " left join CategoryEntity c1 on u.oldCategoryId = c1.id" +
            " left join CategoryEntity c2 on u.newCategoryId = c2.id" +
            " where u.taskId = :taskId" +
            " order by u.modifyTime asc")
    List<UpdateHistoryTaskDTO> getUpdateHistoryTaskEntitiesByTaskId(String taskId);
}
