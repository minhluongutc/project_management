package datn.backend.repositories.jpa;

import datn.backend.entities.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepositoryJPA extends JpaRepository<FilterEntity, String> {

    List<FilterEntity> findByUserIdAndProjectIdAndEnabledOrderByNameDesc(String userId, String projectId, Integer enabled);
}
