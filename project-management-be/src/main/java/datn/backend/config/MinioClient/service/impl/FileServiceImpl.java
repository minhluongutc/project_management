package datn.backend.config.MinioClient.service.impl;

import datn.backend.config.MinioClient.dto.ObjectFileDTO;
import datn.backend.config.MinioClient.service.FileService;
import datn.backend.config.MinioClient.service.MinioService;
import datn.backend.config.MinioClient.utils.Utils;
import datn.backend.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private final MinioService minioService;

    /**
     * Upload file to minio
     *
     * @param tenant
     * @param channel
     * @param files
     * @return
     */
    @Override
    public List<ObjectFileDTO> uploadFiles(String tenant, String channel, MultipartFile[] files) {
        List<ObjectFileDTO> result = new ArrayList<>();
        String rootPath = minioService.getRootFolder(channel);
        for (MultipartFile file : files) {
            try {
                String filePath = rootPath + File.separator + UUID.randomUUID() + "_" + file.getOriginalFilename();
                minioService.uploadFile(tenant, filePath, file.getBytes());
                ObjectFileDTO objectFileDTO = ObjectFileDTO.builder()
                        .linkUrl(Constants.GET_CONTENT_LINK_URL)
                        .linkUrlPublic(Constants.GET_CONTENT_LINK_URL_PUBLIC)
                        .fileName(file.getOriginalFilename())
                        .filePath(Utils.encrypt(filePath))
                        .fileSize(file.getSize())
                        .build();
                result.add(objectFileDTO);
            } catch (Exception e) {
                LOGGER.error("Error uploadFiles: ", e);
            }
        }
        return result;
    }

    /**
     * Get content file
     *
     * @param tenant
     * @param filePath
     * @return
     */
    @Override
    public byte[] getFile(String tenant, String filePath) {
        try {
            return minioService.getFile(tenant, Utils.decrypt(filePath));
        } catch (Exception e) {
            LOGGER.error("Error getFile: ", e);
            return null;
        }
    }
}
