package datn.backend.jpa;

import datn.backend.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepositoryJPA extends JpaRepository<CompanyEntity, String> {
}
