package datn.backend.repositories.jpa;

import datn.backend.dto.PostResponseDTO;
import datn.backend.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryJPA extends JpaRepository<PostEntity, String> {

    @Query("select new datn.backend.dto.PostResponseDTO(" +
            "p.id, p.title, p.content, p.projectId, pj.name, p.createUserId, " +
            "p.createTime, p.updateUserId, p.updateTime, d.id, u.firstName, u.lastName, u.username)" +
            " from PostEntity p" +
            " join UserEntity u on p.createUserId = u.id" +
            " join ProjectEntity pj on pj.id = p.projectId" +
            " left join DocumentEntity d on d.objectId = u.id and d.type = 2 and d.enabled = 1" +
            " where (:projectId is null or :projectId = ''  or (p.projectId = :projectId))" +
            " order by p.createTime asc")
    List<PostResponseDTO> findAllByProjectId(String projectId);
}
