package datn.backend.service;

import datn.backend.dto.TaskDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {

    Object getTasks(Authentication authentication, TaskDTO.TaskQueryDTO dto);
    Object getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto);
    Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files);
    Object updateTask(Authentication authentication, TaskDTO.TaskUpdateDTO dto, String id);
    Object getTask(Authentication authentication, String id);
}
