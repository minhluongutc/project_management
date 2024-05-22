package datn.backend.service;

import datn.backend.dto.UserCalendarRequestDTO;
import org.springframework.security.core.Authentication;

public interface UserCalendarService {
    Object insertUserCalendar(Authentication authentication, UserCalendarRequestDTO dto);

    Object updateUserCalendar(Authentication authentication, UserCalendarRequestDTO dto, String id);

    Object deleteUserCalendar(Authentication authentication, String id);
}
