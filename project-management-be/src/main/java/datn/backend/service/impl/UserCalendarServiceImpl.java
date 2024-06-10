package datn.backend.service.impl;

import datn.backend.dto.TaskDTO;
import datn.backend.dto.UserCalendarRequestDTO;
import datn.backend.dto.UserCalendarResponseDTO;
import datn.backend.entities.UserCalendarEntity;
import datn.backend.repositories.jpa.TaskRepositoryJPA;
import datn.backend.repositories.jpa.UserCalendarRepositoryJPA;
import datn.backend.service.TaskService;
import datn.backend.service.UserCalendarService;
import datn.backend.utils.AuditUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCalendarServiceImpl implements UserCalendarService {
    final TaskRepositoryJPA taskRepositoryJPA;
    final UserCalendarRepositoryJPA userCalendarRepositoryJPA;

    final TaskService taskService;

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

    public Object convertTaskToCalendar(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        dto.setAssignUserId(List.of(AuditUtils.getUserId(authentication)));
        List<TaskDTO.TaskResponseDTO> listTask = taskRepositoryJPA.getTasks(dto);
        List<UserCalendarResponseDTO> userCalendars = new ArrayList<>();
        for (TaskDTO.TaskResponseDTO task: listTask) {
            if (task.getStartDate() != null && task.getDueDate() != null) {
                UserCalendarResponseDTO userCalendar = new UserCalendarResponseDTO();
                userCalendar.setId(task.getId());
                userCalendar.setUserId(AuditUtils.getUserId(authentication));
                userCalendar.setProjectId(task.getProjectId());
                userCalendar.setTaskId(task.getId());
                userCalendar.setTitle(task.getSubject());
                userCalendar.setDescription(task.getDescription());
                userCalendar.setStart(task.getStartDate());
                userCalendar.setEnd(task.getDueDate());
                Calendar calendarDueDate = GregorianCalendar.getInstance();
                calendarDueDate.setTime(task.getDueDate());
                Calendar calendarStartDate = GregorianCalendar.getInstance();
                calendarStartDate.setTime(task.getStartDate());
                userCalendar.setAllDay(calendarStartDate.get(Calendar.HOUR_OF_DAY) == 0 &&
                        calendarStartDate.get(Calendar.MINUTE) == 0 &&
                        calendarDueDate.get(Calendar.HOUR_OF_DAY) == 0 &&
                        calendarDueDate.get(Calendar.MINUTE) == 0);
                userCalendars.add(userCalendar);
            }
        }
        return userCalendars;
    }
}
