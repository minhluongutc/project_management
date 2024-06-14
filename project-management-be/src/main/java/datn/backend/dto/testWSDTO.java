package datn.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class testWSDTO {

    @Data
    @AllArgsConstructor
    public static class ChatMessage {
        String message;
        String user;
    }

}
