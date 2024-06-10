package datn.backend.controllers;

import datn.backend.dto.NotificationQueryDTO;
import datn.backend.service.jpa.NotificationServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {
    final NotificationServiceJPA notificationServiceJPA;

    @GetMapping(value = "notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNotification(Authentication authentication, NotificationQueryDTO dto) {
        Object result = notificationServiceJPA.getNotifications(dto);
        return ResponseUtils.getResponseEntity(result);
    }

    @PutMapping(value = "notifications/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateNotification(Authentication authentication,@PathVariable String id) {
        Object result = notificationServiceJPA.onReadNotify(id);
        return ResponseUtils.getResponseEntity(result);
    }
}
