package datn.backend.jpa;

import datn.backend.config.auth.ERole;
import datn.backend.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositoryJPA extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(ERole name);
}
