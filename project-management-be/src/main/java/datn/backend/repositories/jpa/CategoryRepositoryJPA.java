package datn.backend.repositories.jpa;

import datn.backend.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepositoryJPA extends JpaRepository<CategoryEntity, String> {

    @Query("FROM CategoryEntity c" +
            " WHERE c.projectId = :projectId" +
            " and (:keySearch is null or (" +
            "       lower(c.name) like lower(concat('%', :keySearch, '%')) " +
            "   or  lower(c.description) like lower(concat('%', :keySearch, '%')))" +
            "     )" +
            " and c.enabled = 1" +
            " order by c.createTime desc")
    List<CategoryEntity> getCategories(String projectId, String keySearch);
}
