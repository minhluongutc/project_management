package datn.backend.controllers;

import datn.backend.config.MinioClient.service.FileService;
import datn.backend.config.MinioClient.utils.Utils;
import datn.backend.config.MinioClient.utils.validates.ValidFile;
import datn.backend.utils.ErrorApp;
import datn.backend.utils.ResponseUtils;
import datn.backend.utils.exceptions.CustomException;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static datn.backend.utils.Constants.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@MultipartConfig
@Validated
public class FileController {
    private final FileService fileService;

    @Value("${minio.tenant}")
    String tenant;

    /**
     * Upload file via public
     *
     * @param files
     * @param channel
     * @return
     */
    @PostMapping(REQUEST_MAPPING_PREFIX + VERSION_API_V1 + "/files")
    public ResponseEntity<Object> uploadFilesPublic(@ValidFile @RequestParam("file") MultipartFile[] files,
                                                    @RequestParam("channel") String channel) {
        Object result = fileService.uploadFiles(tenant, channel, files);
        return ResponseUtils.getResponseEntity(result);
    }

    /**
     * Get content file by filePath via public
     *
     * @param filePath
     * @return
     */
    @GetMapping(value = GET_CONTENT_LINK_URL)
    public ResponseEntity<Object> getContentFilePublic(@RequestParam("filePath") String filePath) throws Exception {
        String filePathDecrypt = Utils.decrypt(filePath);
        String fileName = filePathDecrypt.substring(filePathDecrypt.lastIndexOf("/") + 1);
        byte[] data = fileService.getFile(tenant, filePath);
        if (data == null) throw new CustomException(ErrorApp.BAD_REQUEST);
        return datn.backend.config.MinioClient.utils.ResponseUtils.getResponseEntity(data, fileName);
    }
}
