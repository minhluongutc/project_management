package datn.backend.service;

import datn.backend.dto.FilterInsertDTO;
import org.springframework.security.core.Authentication;

public interface FilterService {
    Object updateFilterName(String filterId, String filterName);
    Object insertFilter(Authentication authentication, FilterInsertDTO dto);
    Object deleteFilter(String id);
}
