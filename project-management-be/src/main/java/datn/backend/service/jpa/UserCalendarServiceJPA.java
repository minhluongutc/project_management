package datn.backend.service.jpa;

import datn.backend.dto.UserCalendarResponseDTO;
import datn.backend.entities.UserCalendarEntity;
import datn.backend.repositories.jpa.UserCalendarRepositoryJPA;
import datn.backend.utils.AuditUtils;
import lombok.AllArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserCalendarServiceJPA {
    final UserCalendarRepositoryJPA userCalendarRepositoryJPA;

    public List<UserCalendarResponseDTO> getUserCalendars(Authentication authentication, String projectId) {
        return userCalendarRepositoryJPA.getUserCalendars(AuditUtils.getUserId(authentication), projectId);
    }

    public UserCalendarEntity getUserCalendarById(Authentication authentication, String id) {
        return userCalendarRepositoryJPA.findFirstByUserIdAndId(AuditUtils.getUserId(authentication), id);
    }
}
