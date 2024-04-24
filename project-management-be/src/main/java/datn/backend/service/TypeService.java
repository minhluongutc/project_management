package datn.backend.service;

import datn.backend.dto.TypeDTO;
import datn.backend.entities.TypeEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TypeService {
    Object insertType(Authentication authentication, @RequestBody TypeDTO.TypeRequestDTO dto);
    Object updateType(Authentication authentication, @RequestBody TypeDTO.TypeRequestDTO dto, String id);
    Object deleteType(Authentication authentication, String id);
}
