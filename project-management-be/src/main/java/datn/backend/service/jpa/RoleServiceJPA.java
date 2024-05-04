package datn.backend.service.jpa;

import datn.backend.entities.RoleEntity;
import datn.backend.repositories.jpa.RoleRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceJPA {
    final RoleRepositoryJPA roleRepositoryJPA;

    public List<RoleEntity> getAllRoles() {
        return roleRepositoryJPA.findAll();
    }
}
