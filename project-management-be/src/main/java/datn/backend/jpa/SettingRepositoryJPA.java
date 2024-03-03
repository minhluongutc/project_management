package datn.backend.jpa;

import datn.backend.entities.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepositoryJPA extends JpaRepository<SettingEntity, String> {
}
