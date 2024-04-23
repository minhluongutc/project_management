package datn.backend.repositories.jpa;

import datn.backend.entities.ProjectUserEntity;
import datn.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryJPA extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("SELECT u.id FROM UserEntity u WHERE u.username = ?1 and u.enabled=1")
    String getIdByUsername(String username);

    Optional<UserEntity> findByIdAndEnabled(String id, Integer enabled);

    List<UserEntity> getUserEntitiesByEnabled(Integer enabled);

    @Query("SELECT u.companyId FROM UserEntity u WHERE u.username = ?1 and u.enabled=1")
    String getCompanyIdByUsername(String username);
//    String getCompanyIdByUsername(String name);

}
