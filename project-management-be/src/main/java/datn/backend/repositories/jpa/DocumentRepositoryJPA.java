package datn.backend.repositories.jpa;

import datn.backend.dto.AttachmentDTO;
import datn.backend.dto.AttachmentDTO.AttachmentResponseDTO;
import datn.backend.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepositoryJPA extends JpaRepository<DocumentEntity, String> {
    @Query("select new datn.backend.dto.AttachmentDTO$AttachmentResponseDTO(d.id, d.fileName, d.filePath, d.fileSize) " +
            " from DocumentEntity d" +
            " where d.objectId = :objectId" +
            " and d.enabled = 1")
    List<AttachmentResponseDTO> getAttachmentsByObjectId(String objectId);

    @Query("select new datn.backend.dto.AttachmentDTO$AttachmentResponsePrimengDTO(d.id, d.fileName, d.filePath, d.fileSize) " +
            " from DocumentEntity d" +
            " where d.objectId = :objectId" +
            " and d.enabled = 1")
    List<AttachmentDTO.AttachmentResponsePrimengDTO> getAttachmentsByObjectIdPremeNG(String objectId);

    @Query("select d.filePath from DocumentEntity d where d.id = :id and d.enabled = 1")
    Optional<String> getFilePathById(String id);

}