package datn.backend.config.MinioClient.utils;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import java.time.Duration;

public class ResponseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);

    public static ResponseEntity<Object> getResponseEntity(byte[] data, String fileName) {
        Tika tika = new Tika();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().build());
        headers.set("fileName", fileName);
        headers.set("Content-Type", tika.detect(fileName));
        headers.set("X-Frame-Options", "SAMEORIGIN");
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "fileName");
        headers.setCacheControl(CacheControl.maxAge(Duration.ofSeconds(3600)).getHeaderValue());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
