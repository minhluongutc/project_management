package datn.backend.service.jpa;

import datn.backend.dto.AttachmentDTO;
import datn.backend.entities.DocumentEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentServiceJPA {
    private final DocumentRepositoryJPA documentRepositoryJPA;

    public Optional<DocumentEntity> findDocumentById(String id) {
        return documentRepositoryJPA.findById(id);
    }

    public List<DocumentEntity> getAllDocuments() {
        return documentRepositoryJPA.findAll();
    }

    public List<AttachmentDTO.AttachmentResponsePrimengDTO> getAttachmentsByObjectIdAndType(String objectId, Integer type) {
        List<AttachmentDTO.AttachmentResponsePrimengDTO> attachmentResponsePrimengDTO = new ArrayList<>();
        List<AttachmentDTO.AttachmentResponseDTO> attachmentResponseDTO = documentRepositoryJPA.getAttachmentsByObjectIdAndType(objectId, type);
        String fileType = "unknown";
        for (AttachmentDTO.AttachmentResponseDTO attachment : attachmentResponseDTO) {
            fileType = attachment.getFileName().substring(attachment.getFileName().lastIndexOf(".") + 1);
            attachmentResponsePrimengDTO.add(new AttachmentDTO.AttachmentResponsePrimengDTO(attachment.getId(), attachment.getFileName(), attachment.getFilePath(), attachment.getFileSize(), fileType));
        }
        return attachmentResponsePrimengDTO;
    }
}
