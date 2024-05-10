package datn.backend.service.jpa;

import datn.backend.dto.UpdateHistoryTaskDTO;
import datn.backend.repositories.jpa.UpdateHistoryTaskRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateHistoryTaskServiceJPA {
    final UpdateHistoryTaskRepositoryJPA updateHistoryTaskRepositoryJPA;

    public List<UpdateHistoryTaskDTO> getUpdateHistoryTaskEntitiesByTaskId(String taskId) {
        return updateHistoryTaskRepositoryJPA.getUpdateHistoryTaskEntitiesByTaskId(taskId);
    }
}
