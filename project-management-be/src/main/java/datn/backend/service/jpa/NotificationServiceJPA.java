package datn.backend.service.jpa;

import datn.backend.dto.NotificationQueryDTO;
import datn.backend.dto.NotificationResponseDTO;
import datn.backend.entities.NotificationEntity;
import datn.backend.repositories.jpa.NotificationRepositoryJPA;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceJPA {
    private final NotificationRepositoryJPA notificationRepositoryJPA;

    public List<NotificationResponseDTO> getNotifications(NotificationQueryDTO dto) {
        return notificationRepositoryJPA.getNotifications(dto);
    }

    @Transactional
    public Object onReadNotify(String id) {
        NotificationEntity notification = notificationRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(Constants.IS_READ.YES.value);
        notification.setUpdateTime(AuditUtils.updateTime());
        notificationRepositoryJPA.save(notification);
        return "success";
    }
}
