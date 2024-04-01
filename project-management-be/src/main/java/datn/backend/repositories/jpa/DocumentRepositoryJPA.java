package datn.backend.repositories.jpa;

import datn.backend.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepositoryJPA extends JpaRepository<DocumentEntity, Integer> {
}
