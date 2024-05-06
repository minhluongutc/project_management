package datn.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UploadMessageDTO {
    private String messageCode;
    private String messageDesc;
    private String fileData;
    private Integer totalSuccess;
    private Integer totalError;
    private Object dtoList;
}
