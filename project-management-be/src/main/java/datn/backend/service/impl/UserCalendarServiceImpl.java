package datn.backend.service.impl;

import datn.backend.dto.UserCalendarRequestDTO;
import datn.backend.entities.UserCalendarEntity;
import datn.backend.repositories.jpa.UserCalendarRepositoryJPA;
import datn.backend.service.UserCalendarService;
import datn.backend.utils.AuditUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCalendarServiceImpl implements UserCalendarService {
    final UserCalendarRepositoryJPA userCalendarRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object insertUserCalendar(Authentication authentication, UserCalendarRequestDTO dto) {
        UserCalendarEntity userCalendarEntity = new UserCalendarEntity();
        modelMapper.map(dto, userCalendarEntity);
        userCalendarEntity.setId(AuditUtils.generateUUID());
        userCalendarEntity.setUserId(AuditUtils.getUserId(authentication));
        userCalendarRepositoryJPA.save(userCalendarEntity);
        return "Insert user calendar success!";
    }

    @Override
    public Object updateUserCalendar(Authentication authentication, UserCalendarRequestDTO dto, String id) {
        UserCalendarEntity userCalendarEntity = userCalendarRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("User calendar not found!"));
        if (dto.getChangeDateOnly()) {
            userCalendarEntity.setAllDay(dto.getAllDay());
            userCalendarEntity.setStart(dto.getStart());
            userCalendarEntity.setEnd(dto.getEnd());
        } else {
            modelMapper.map(dto, userCalendarEntity);
            userCalendarEntity.setId(id);
            userCalendarEntity.setUserId(AuditUtils.getUserId(authentication));
        }
        userCalendarRepositoryJPA.save(userCalendarEntity);
        return "Update user calendar success!";
    }

    @Override
    public Object deleteUserCalendar(Authentication authentication, String id) {
        userCalendarRepositoryJPA.deleteById(id);
        return "Delete user calendar success!";
    }
}
