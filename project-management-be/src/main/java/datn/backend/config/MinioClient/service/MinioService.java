package datn.backend.config.MinioClient.service;

public interface MinioService {
    boolean uploadFile(String tenant, String filePath, byte[] file);

    boolean removeFile(String tenant, String filePath);

    byte[] getFile(String tenant, String filePath);

    String getRootFolder(String channel);

    boolean copyFile(String tenant, String sourceFilePath, String targetFilePath);
}