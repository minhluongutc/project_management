package datn.backend.jpa;

import datn.backend.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryJPA extends JpaRepository<RoleEntity, String> {
}
