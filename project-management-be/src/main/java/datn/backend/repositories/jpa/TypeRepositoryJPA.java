package datn.backend.repositories.jpa;

import datn.backend.entities.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepositoryJPA extends JpaRepository<TypeEntity, String> {
    List<TypeEntity> getTypeEntitiesByProjectIdAndEnabled(String projectId, Integer enabled);
}
