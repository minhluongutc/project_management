package datn.backend.service.impl;

import datn.backend.config.MinioClient.dto.ObjectFileDTO;
import datn.backend.config.MinioClient.service.FileService;
import datn.backend.entities.DocumentEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import datn.backend.service.DocumentService;
import datn.backend.service.jpa.DocumentServiceJPA;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentServiceImpl implements DocumentService {
    final FileService fileService;
    final DocumentRepositoryJPA documentRepositoryJPA;
    final DocumentServiceJPA documentServiceJPA;

    @Override
    @Transactional
    public Object addAttachment(Authentication authentication, String objectId, MultipartFile file, Integer type) {
        ObjectFileDTO objectFileDTO = fileService.uploadFiles(Constants.FILE_MINIO_TENANT, Constants.FILE_MINIO_CHANNEL, new MultipartFile[]{file}).get(0);
        if (objectFileDTO != null) {
            DocumentEntity documentEntity = new DocumentEntity();
            documentEntity.setId(AuditUtils.generateUUID());
            documentEntity.setObjectId(objectId);
            documentEntity.setFileName(objectFileDTO.getFileName());
            documentEntity.setFilePath(objectFileDTO.getFilePath());
            documentEntity.setFileSize(objectFileDTO.getFileSize());
            documentEntity.setType(type);
            documentEntity.setEnabled(AuditUtils.enable());
            documentEntity.setCreateUserId(AuditUtils.createUserId(authentication));
            documentEntity.setCreateTime(AuditUtils.createTime());

            // insert table document
            documentRepositoryJPA.save(documentEntity);

            return "Thành công";
        }
        return null;
    }

    @Override
    @Transactional
    public Object addAttachments(Authentication authentication, String objectId, MultipartFile[] files, Integer type) {
        List<ObjectFileDTO> objectFileDTOs = fileService.uploadFiles(Constants.FILE_MINIO_TENANT, Constants.FILE_MINIO_CHANNEL, files);
        if (objectFileDTOs != null && !objectFileDTOs.isEmpty()) {
            for (ObjectFileDTO objectFileDTO : objectFileDTOs) {
                DocumentEntity documentEntity = new DocumentEntity();
                documentEntity.setId(AuditUtils.generateUUID());
                documentEntity.setObjectId(objectId);
                documentEntity.setFileName(objectFileDTO.getFileName());
                documentEntity.setFilePath(objectFileDTO.getFilePath());
                documentEntity.setFileSize(objectFileDTO.getFileSize());
                documentEntity.setType(type);
                documentEntity.setEnabled(AuditUtils.enable());
                documentEntity.setCreateUserId(AuditUtils.createUserId(authentication));
                documentEntity.setCreateTime(AuditUtils.createTime());

                // insert table document
                documentRepositoryJPA.save(documentEntity);
            }
            return "Thành công";
        }
        return null;
    }

    @Override
    @Transactional
    public Object deleteAttachment(Authentication authentication, String id) {
        DocumentEntity documentEntity = documentServiceJPA.findDocumentById(id).orElseThrow(() -> new RuntimeException("Document not found!"));
        documentEntity.setEnabled(AuditUtils.disable());
        documentEntity.setUpdateUserId(AuditUtils.createUserId(authentication));
        documentEntity.setUpdateTime(AuditUtils.updateTime());
        documentRepositoryJPA.save(documentEntity);
        return "Deleted successfully!";
    }

}
