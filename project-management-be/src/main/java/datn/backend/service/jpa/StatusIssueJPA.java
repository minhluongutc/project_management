package datn.backend.service.jpa;

import datn.backend.entities.StatusIssueEntity;
import datn.backend.repositories.jpa.StatusIssueRepositoryJPA;
import datn.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusIssueJPA {
    final StatusIssueRepositoryJPA statusIssueRepositoryJPA;

    public List<StatusIssueEntity> getStatusIssue(String projectId, String keySearch) {
        return statusIssueRepositoryJPA.getStatusIssue(projectId, keySearch);
    }

}
