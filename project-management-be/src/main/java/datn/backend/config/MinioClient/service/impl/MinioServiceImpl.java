package datn.backend.config.MinioClient.service.impl;

import datn.backend.config.MinioClient.MinioClientConfiguration;
import datn.backend.config.MinioClient.service.MinioService;
import io.minio.*;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

@Service
public class MinioServiceImpl implements MinioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioServiceImpl.class);
    private final MinioClient minioClient;

    public MinioServiceImpl(MinioClientConfiguration config) {
        this.minioClient = MinioClient.builder().endpoint(config.getBaseUrl()).credentials(config.getAccessKey(), config.getSecretKey()).build();
    }

    @Override
    public boolean uploadFile(String tenant, String filePath, byte[] file) {
        try {
            filePath = sanitizeObjectName(filePath);
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }
            createBucketIfNotExist(tenant);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(tenant)
                    .object(filePath)
                    .stream(new ByteArrayInputStream(file), file.length, -1)
                    .build());
        } catch (Exception e) {
            LOGGER.error("Error uploadFile: ", e);
            return false;
        }
        return true;
    }

    private void createBucketIfNotExist(String bucketName) throws Exception {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).objectLock(false).build());
        }
    }

    @Override
    public boolean removeFile(String tenant, String filePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(tenant).object(filePath).build());
        } catch (Exception e) {
            LOGGER.error("Error removeFile: ", e);
            return false;
        }
        return true;
    }

    @Override
    public byte[] getFile(String tenant, String filePath) {
        filePath = sanitizeObjectName(filePath);
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(tenant)
                        .object(filePath)
                        .build())) {
            return IOUtils.toByteArray(stream);
        } catch (Exception e) {
            LOGGER.error("Error getFile: ", e);
        }
        return null;
    }

    @Override
    public String getRootFolder(String channel) {
        Calendar calendar = Calendar.getInstance();
        return channel + File.separator + calendar.get(Calendar.YEAR) + File.separator + (calendar.get(Calendar.MONTH) + 1) + File.separator + calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean copyFile(String tenant, String sourceFilePath, String targetFilePath) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(tenant)
                            .object(targetFilePath)
                            .source(CopySource.builder().bucket(tenant).object(sourceFilePath).build())
                            .build());
            return true;
        } catch (Exception e) {
            LOGGER.error("Error copyFile: ", e);
            return false;
        }
    }

    private String sanitizeObjectName(String objectName) {
        return objectName.replaceAll("[*:|?\"<>\\\\]", "_");
    }
}
