package datn.backend.jpa;

import datn.backend.entities.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepositoryJPA extends JpaRepository<ProjectUserEntity, String> {
}
