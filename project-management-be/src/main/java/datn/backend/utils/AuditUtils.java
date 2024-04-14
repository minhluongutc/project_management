package datn.backend.utils;

import datn.backend.repositories.jpa.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Configuration
public class AuditUtils {
    private static UserRepositoryJPA userRepositoryJPA;

    @Autowired
    public AuditUtils(UserRepositoryJPA userRepositoryJPA) {
        AuditUtils.userRepositoryJPA = userRepositoryJPA;
    }

    public static Date createTime() {
        return new Date();
    }

    public static Date updateTime() {
        return new Date();
    }

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String createUserId(Authentication authentication) {
        return userRepositoryJPA.getIdByUsername(authentication.getName());
    }

    public static String updateUserId(Authentication authentication) {
        return userRepositoryJPA.getIdByUsername(authentication.getName());
    }

    public static Integer enable() {
        return Constants.STATUS.ACTIVE.value;
    }

    public static Integer disable() {
        return Constants.STATUS.IN_ACTIVE.value;
    }
}
