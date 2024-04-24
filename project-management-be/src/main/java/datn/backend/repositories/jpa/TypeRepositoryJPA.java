package datn.backend.repositories.jpa;

import datn.backend.entities.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepositoryJPA extends JpaRepository<TypeEntity, String> {

    @Query("FROM TypeEntity t" +
            " WHERE t.projectId = :projectId" +
            " and (:keySearch is null or (" +
            "       lower(t.name) like lower(concat('%', :keySearch, '%')) " +
            "   or  lower(t.description) like lower(concat('%', :keySearch, '%')))" +
            "     )" +
            " and t.enabled = 1" +
            " order by t.createTime desc")
    List<TypeEntity> getTypes(String projectId, String keySearch);
}
