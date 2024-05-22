package datn.backend.service.jpa;

import datn.backend.entities.FilterEntity;
import datn.backend.repositories.jpa.FilterRepositoryJPA;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FilterServiceJPA {
    private final FilterRepositoryJPA filterRepositoryJPA;

    public List<FilterEntity> findByUserIdAndProjectIdAndEnabled(Authentication authentication, String projectId) {
        return filterRepositoryJPA.findByUserIdAndProjectIdAndEnabledOrderByNameDesc(AuditUtils.getUserId(authentication), projectId, Constants.STATUS.ACTIVE.value);
    }
}
