package datn.backend.controllers;

import datn.backend.dto.NotificationInsertDTO;
import datn.backend.dto.UserInfoDTO;
import datn.backend.dto.testWSDTO;
import datn.backend.entities.NotificationEntity;
import datn.backend.repositories.jpa.NotificationRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.EmailSenderService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSocketController {
    final EmailSenderService senderService;
    final NotificationRepositoryJPA notificationRepositoryJPA;
    final UserRepositoryJPA userRepositoryJPA;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public testWSDTO.ChatMessage chat(@DestinationVariable String roomId, testWSDTO.ChatMessage message) {
        System.out.println(message);
        return new testWSDTO.ChatMessage(message.getMessage(), message.getUser());
    }

    @MessageMapping("/notification/{roomId}")
    @SendTo("/topic/{roomId}")
    public Object notify(@DestinationVariable String roomId, NotificationInsertDTO dto) {
        System.out.println(dto);
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(AuditUtils.generateUUID());
        notificationEntity.setUserId(dto.getToUserId());
        notificationEntity.setTaskId(dto.getTaskId());
//        notificationEntity.setOptionalContent(dto.getContent());
        notificationEntity.setType(dto.getActionType());
        notificationEntity.setIsRead(Constants.IS_READ.NO.value);
        notificationEntity.setCreateUserId(dto.getFromUserId());
        notificationEntity.setCreateTime(AuditUtils.createTime());
        notificationEntity.setEnabled(Constants.STATUS.ACTIVE.value);
        notificationRepositoryJPA.save(notificationEntity);
        System.out.println(notificationEntity);

        UserInfoDTO userReceive = userRepositoryJPA.getUserById(dto.getToUserId());
        senderService.sendEmail(userReceive.getEmail(), "Notification", getContentFromActionType(dto.getActionType()));
        return notificationEntity;
    }

    private String getContentFromActionType(Integer actionType) {
        return switch (actionType) {
            case 1 -> Constants.NOTIFICATION_TYPE.ADD_TASK.textValue;
            case 2 -> Constants.NOTIFICATION_TYPE.EDIT_TASK.textValue;
            default -> "";
        };
    }

}
