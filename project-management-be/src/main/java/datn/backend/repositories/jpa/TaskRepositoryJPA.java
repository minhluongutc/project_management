package datn.backend.repositories.jpa;

import datn.backend.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepositoryJPA extends JpaRepository<TaskEntity, Integer> {
}
