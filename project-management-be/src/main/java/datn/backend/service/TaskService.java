package datn.backend.service;

import datn.backend.dto.TaskDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {
    Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files);

    Object getTasksByProjectId(Authentication authentication, TaskDTO.TaskQueryDTO dto);
}
