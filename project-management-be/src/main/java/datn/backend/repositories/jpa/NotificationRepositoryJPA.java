package datn.backend.repositories.jpa;

import datn.backend.dto.NotificationQueryDTO;
import datn.backend.dto.NotificationResponseDTO;
import datn.backend.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryJPA extends JpaRepository<NotificationEntity, String> {

    @Query("select new datn.backend.dto.NotificationResponseDTO("+
            " n.id, n.userId, n.createUserId, n.taskId, t.taskCode, p.name, n.optionalContent," +
            " n.type, d.id, u.username, n.createTime, n.isRead)" +
            " from NotificationEntity n" +
            " join UserEntity u on n.createUserId = u.id" +
            " left join DocumentEntity d on u.id = d.objectId and d.type = 2" +
            " left join TaskEntity t on t.id = n.taskId and t.enabled = 1" +
            " left join ProjectEntity p on p.id = t.projectId" +
            " where n.userId = :#{#dto.userId}" +
            " and (:#{#dto.actionType} is null or n.type = :#{#dto.actionType})" +
            " and (:#{#dto.isRead} is null or n.isRead = :#{#dto.isRead})" +
            " order by n.createTime desc")
    List<NotificationResponseDTO> getNotifications(@Param("dto") NotificationQueryDTO dto);

//    @Modifying
//    @Query("update NotificationEntity n set n.isRead = true where n.id = :id")
//    void onReadNotify(@Param("id") String id, @Param("time") String time);
}
