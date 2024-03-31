package datn.backend.config.MinioClient;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinioClientConfiguration {
    String baseUrl;
    String accessKey;
    String secretKey;
}