package datn.backend.service.jpa;

import datn.backend.dto.AttachmentDTO;
import datn.backend.entities.DocumentEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentServiceJPA {
    private final DocumentRepositoryJPA documentRepositoryJPA;

    public List<DocumentEntity> getAllDocuments() {
        return documentRepositoryJPA.findAll();
    }

    public List<AttachmentDTO.AttachmentResponsePrimengDTO> getAttachmentsByObjectId(String objectId) {
        List<AttachmentDTO.AttachmentResponsePrimengDTO> attachmentResponsePrimengDTO = new ArrayList<>();
        List<AttachmentDTO.AttachmentResponseDTO> attachmentResponseDTO = documentRepositoryJPA.getAttachmentsByObjectId(objectId);
        String fileType = "unknown";
        for (AttachmentDTO.AttachmentResponseDTO attachment : attachmentResponseDTO) {
            fileType = attachment.getFileName().substring(attachment.getFileName().lastIndexOf(".") + 1);
            attachmentResponsePrimengDTO.add(new AttachmentDTO.AttachmentResponsePrimengDTO(attachment.getId(), attachment.getFileName(), attachment.getFilePath(), attachment.getFileSize(), fileType));
        }
        return attachmentResponsePrimengDTO;
    }
}
