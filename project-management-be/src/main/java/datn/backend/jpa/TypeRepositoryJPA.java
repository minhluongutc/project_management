package datn.backend.jpa;

import datn.backend.entities.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepositoryJPA extends JpaRepository<TypeEntity, String> {
}
