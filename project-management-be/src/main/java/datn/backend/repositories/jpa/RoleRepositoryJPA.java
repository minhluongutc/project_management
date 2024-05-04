package datn.backend.repositories.jpa;

import datn.backend.config.auth.ERole;
import datn.backend.entities.RoleEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepositoryJPA extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(ERole name);
    @NotNull
    Optional<RoleEntity> findById(String id);
//    @NotNull
//    List<RoleEntity> findAll();
}
