package datn.backend.repositories.jpa;

import datn.backend.entities.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepositoryJPA extends JpaRepository<TypeEntity, Integer> {
    List<TypeEntity> getTypeEntitiesByProjectIdAndEnabled(Integer projectId, Integer enabled);
}
