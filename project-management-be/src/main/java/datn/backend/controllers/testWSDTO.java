package datn.backend.controllers;

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
