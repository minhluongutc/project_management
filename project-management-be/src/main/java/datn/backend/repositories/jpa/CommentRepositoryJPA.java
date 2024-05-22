package datn.backend.repositories.jpa;

import datn.backend.dto.CommentResponseDTO;
import datn.backend.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryJPA extends JpaRepository<CommentEntity, String> {

    @Query("select new datn.backend.dto.CommentResponseDTO(" +
            " c.id, c.objectId, c.content, c.createUserId, c.createTime, c.updateUserId, c.updateTime, d.id, u.username) " +
            " from CommentEntity c" +
            " join UserEntity u on c.createUserId = u.id" +
            " left join DocumentEntity d on u.id = d.objectId and d.type = 2 and d.enabled = 1" +
            " where c.objectId = ?1 and c.enabled = 1" +
            " order by c.createTime asc")
    List<CommentResponseDTO> getCommentEntitiesByObjectIdAndEnabled(String objectId);

}
