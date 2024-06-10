//package datn.backend.ws;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Slf4j
//public class DataHandler extends TextWebSocketHandler {
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
//        log.info("Message: {}", message.getPayload());
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        // Perform actions when a new WebSocket connection is established
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        // Perform actions when a WebSocket connection is closed
//    }
//}
