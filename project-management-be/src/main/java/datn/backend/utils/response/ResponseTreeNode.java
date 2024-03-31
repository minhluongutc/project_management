package datn.backend.utils.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseTreeNode<T> implements Serializable {
    T data;

    public ResponseTreeNode(T data) {
        this.data = data;
    }
}
