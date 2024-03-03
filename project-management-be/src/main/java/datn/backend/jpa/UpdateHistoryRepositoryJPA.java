package datn.backend.jpa;

import datn.backend.entities.UpdateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateHistoryRepositoryJPA extends JpaRepository<UpdateHistoryEntity, String> {
}
