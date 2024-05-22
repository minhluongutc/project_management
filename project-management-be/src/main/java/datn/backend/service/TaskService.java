package datn.backend.service;

import datn.backend.dto.TaskDTO;
import datn.backend.dto.UploadMessageDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {

    Object getTasks(Authentication authentication, TaskDTO.TaskQueryDTO dto);
    Object getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto);
    Object getTasksChildrenByParentId(Authentication authentication, String parentId);
    Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files);
    Object updateTask(Authentication authentication, TaskDTO.TaskUpdateDTO dto, String id);
    Object getTask(Authentication authentication, String id);
    Object getTasksChildren(Authentication authentication, String parentId);

    void downloadTemplate(Authentication authentication, String projectId, HttpServletResponse response);
    UploadMessageDTO importTasks(Authentication authentication, String projectId, MultipartFile file);
}
