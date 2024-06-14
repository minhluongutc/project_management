package datn.backend.repositories.jpa;

import datn.backend.dto.UserCalendarResponseDTO;
import datn.backend.entities.UserCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCalendarRepositoryJPA extends JpaRepository<UserCalendarEntity, String> {

    @Query("select new datn.backend.dto.UserCalendarResponseDTO(" +
            " uc.id, uc.userId, uc.projectId, uc.taskId, uc.title, uc.description, uc.start, uc.end, uc.allDay, si.code" +
            ") " +
            " from UserCalendarEntity uc " +
            " join TaskEntity t on t.id = uc.taskId" +
            " join StatusIssueEntity si on si.id = t.statusIssueId"+
            " where uc.userId = :userId " +
            " and :projectId is null or :projectId = '' or uc.projectId = :projectId")
    List<UserCalendarResponseDTO> getUserCalendars(String userId, String projectId);

    UserCalendarEntity findFirstByUserIdAndId(String UserId, String Id);
}
