package datn.backend.utils;

public enum ErrorApp {
    SUCCESS(200, "msg.success"),
    BAD_REQUEST(400, "msg.bad.request"),
    BAD_REQUEST_PATH(400, "msg.bad.request.path"),
    UNAUTHORIZED(401, "msg.unauthorized"),

    NOT_FOUND(402, "msg.not.found"),
    FORBIDDEN(403, "msg.access.denied"),
    INTERNAL_SERVER(500, "msg.internal.server"),
    SURVEY_PARAM_NOT_FOUND(4001, "msg.common.not.found"),
    SURVEY_PARAM_ATTACHMENT_NOT_FOUND(4002, "msg.common.not.found");

    private final int code;
    private final String description;

    ErrorApp(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
