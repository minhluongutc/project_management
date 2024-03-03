package datn.backend.jpa;

import datn.backend.entities.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepositoryJPA extends JpaRepository<LabelEntity, String> {
}
