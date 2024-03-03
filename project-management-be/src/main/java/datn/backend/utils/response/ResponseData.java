package datn.backend.utils.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseData<T> implements Serializable {

    static final long serialVersionUID = 1L;

    final String timestamp;

    final String clientMessageId;

    final String transactionId;

    int code;

    String message;

    T data;

    public ResponseData(String clientMessageId, String transactionId) {
        this.code = 200;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.message = "Successful!";
        this.clientMessageId = clientMessageId;
        this.transactionId = transactionId;
    }

    public ResponseData<T> success(T data) {
        this.data = data;
        return this;
    }

    public ResponseData<T> error(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public ResponseData<T> error(int code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
        return this;
    }

    public void setData(T data) {
        this.data = data;
    }

}
