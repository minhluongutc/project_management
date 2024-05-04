package datn.backend.repositories.jpa;

import datn.backend.dto.UserInfoDTO;
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

    @Query("SELECT u.id FROM UserEntity u WHERE u.email = ?1 and u.enabled=1")
    Optional<String> getIdByEmail(String email);

    Optional<UserEntity> findByIdAndEnabled(String id, Integer enabled);

    List<UserEntity> getUserEntitiesByEnabled(Integer enabled);

    @Query("select new datn.backend.dto.UserInfoDTO(" +
            " u.id, u.username, u.password, u.settingId, u.firstName, " +
            " u.lastName, u.contact, u.email, u.gender, d.filePath, " +
            " d2.filePath, u.dateOfBirth, u.address, u.createUserId, " +
            " u.createTime, u.updateUserId, u.updateTime ) " +
            " from UserEntity u" +
            " left join DocumentEntity d on d.objectId = u.avatarId " +
            " left join DocumentEntity d2 on d2.objectId = u.backgroundId " +
            " where u.id = ?1 and u.enabled = 1")
    UserInfoDTO getUserById(String id);

}
