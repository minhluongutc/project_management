package datn.backend.utils;

import lombok.Getter;

@Getter
public enum ErrorApp {
    SUCCESS(200, "msg.success"),
    BAD_REQUEST(400, "msg.bad.request"),
    BAD_REQUEST_PATH(400, "msg.bad.request.path"),
    UNAUTHORIZED(401, "msg.unauthorized"),

    NOT_FOUND(402, "msg.not.found"),
    FORBIDDEN(403, "msg.access.denied"),
    INTERNAL_SERVER(500, "msg.internal.server"),
    CATEGORY_IN_USE(4001, "CATEGORY_IN_USE"),
    STATUS_ISSUE_IN_USE(4002, "CATEGORY_IN_USE"),
    TYPE_IN_USE(4003, "CATEGORY_IN_USE");

    private final int code;
    private final String description;

    ErrorApp(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
