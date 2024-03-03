package datn.backend.jpa;

import datn.backend.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepositoryJPA extends JpaRepository<ProjectEntity, String> {
}
