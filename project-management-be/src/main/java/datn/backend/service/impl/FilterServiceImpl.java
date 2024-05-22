package datn.backend.service.impl;

import datn.backend.dto.FilterInsertDTO;
import datn.backend.entities.FilterEntity;
import datn.backend.repositories.jpa.FilterRepositoryJPA;
import datn.backend.service.FilterService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterServiceImpl implements FilterService {
    final FilterRepositoryJPA filterRepositoryJPA;

    final ModelMapper modelMapper;

    @Override
    public Object updateFilterName(String filterId, String filterName) {
        FilterEntity filterEntity = filterRepositoryJPA.findById(filterId).orElseThrow(() -> new RuntimeException("Filter not found"));
        filterEntity.setName(filterName);
        filterEntity.setUpdateTime(AuditUtils.updateTime());
        filterRepositoryJPA.save(filterEntity);

        return "update filter name success";
    }

    @Override
    public Object insertFilter(Authentication authentication, FilterInsertDTO dto) {
        FilterEntity filterEntity = new FilterEntity();
        modelMapper.map(dto, filterEntity);
        filterEntity.setCreateTime(AuditUtils.createTime());
        filterEntity.setId(AuditUtils.generateUUID());
        filterEntity.setUserId(AuditUtils.getUserId(authentication));
        filterEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        filterRepositoryJPA.save(filterEntity);
        return "insert filter success";
    }

    @Override
    public Object deleteFilter(String id) {
        FilterEntity filterEntity = filterRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Filter not found"));
        filterEntity.setEnabled(Constants.STATUS.IN_ACTIVE.value);
        filterRepositoryJPA.save(filterEntity);
        return "delete filter success";
    }
}
