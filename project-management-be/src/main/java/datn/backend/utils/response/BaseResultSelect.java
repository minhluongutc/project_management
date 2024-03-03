package datn.backend.utils.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResultSelect {
    List<? extends Object> listData;
    Object count;

    public BaseResultSelect() {

    }

    public BaseResultSelect(List<? extends Object> listData, Object count) {
        this.listData = listData;
        this.count = count;
    }
}
