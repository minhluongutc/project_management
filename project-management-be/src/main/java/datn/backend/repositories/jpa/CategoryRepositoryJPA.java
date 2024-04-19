package datn.backend.repositories.jpa;

import datn.backend.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepositoryJPA extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> getCategoryEntitiesByProjectIdAndEnabled(String projectId, Integer enabled);
}
