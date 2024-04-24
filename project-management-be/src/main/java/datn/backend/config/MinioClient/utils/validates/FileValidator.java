package datn.backend.config.MinioClient.utils.validates;

import datn.backend.config.MinioClient.utils.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile[]> {
    @Value(value = "${app.validate.image.file-size:10000000}")
    public Integer allowImageFileSize;

    @Value(value = "${app.validate.mp3.file-size:15000000}")
    public Integer allowMp3FileSize;

    @Value(value = "${app.validate.mp4.file-size:20000000}")
    public Integer allowMp4FileSize;

    @Value(value = "${app.validate.other.file-size:10000000}")
    public Integer allowOtherFileSize;

    @Value(value = "#{'${app.validate.image.allowExt:PNG,JPG,JPEG,JPEC,HPEG,GIF}'.split(',')}")
    public List<String> allowImageTypes;

    @Value(value = "#{'${app.validate.mp3.allowExt:MP3,WAV}'.split(',')}")
    public List<String> allowMp3Types;

    @Value(value = "#{'${app.validate.mp4.allowExt:MP4,MOV,AVI}'.split(',')}")
    public List<String> allowMp4Types;

    @Value(value = "#{'${app.validate.other.allowExt:PDF,PPTX,TXT,RAR,ZIP,DOC,DOCX,XLS,XLSX,EXE}'.split(',')}")
    public List<String> allowOtherTypes;

    @Override
    public void initialize(ValidFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFiles == null || multipartFiles.length == 0) return false;
        boolean isValid = true;
        try {
            for (MultipartFile multipartFile : multipartFiles) {

                String fileName = multipartFile.getOriginalFilename();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (this.allowImageTypes.contains(fileExt.toUpperCase())) {
                    isValid = Utils.checkFileSizeValid(multipartFile.getBytes(), allowImageFileSize);
                } else if (this.allowMp3Types.contains(fileExt.toUpperCase())) {
                    isValid = Utils.checkFileSizeValid(multipartFile.getBytes(), allowMp3FileSize);
                } else if (this.allowMp4Types.contains(fileExt.toUpperCase())) {
                    isValid = Utils.checkFileSizeValid(multipartFile.getBytes(), allowMp4FileSize);
                } else if (this.allowOtherTypes.contains(fileExt.toUpperCase())) {
                    isValid = Utils.checkFileSizeValid(multipartFile.getBytes(), allowOtherFileSize);
                } else {
                    isValid = false;
                }

                if (!isValid) break;
            }
            return isValid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}