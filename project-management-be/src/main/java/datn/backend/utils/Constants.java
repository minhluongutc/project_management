package datn.backend.utils;

public class Constants {
    public static final String PUBLIC_REQUEST_MAPPING_PREFIX = "/public/api";
    public static final String REQUEST_MAPPING_PREFIX = "/api";
    public static final String VERSION_API_V1 = "/v1";
    public static final String GET_CONTENT_LINK_URL = REQUEST_MAPPING_PREFIX  + "/files/content";
    public static final String GET_CONTENT_LINK_URL_PUBLIC = PUBLIC_REQUEST_MAPPING_PREFIX  + "/files/content";
    public static final String LOCALE_VN = "vi_VN";
    public static final String TIMEZONE_VN = "Asia/Ho_Chi_Minh";
    public static final String DATE_STRING_FORMAT = "dd/MM/yyyy";

    public static final String FILE_MINIO_TENANT = "jiraclone";
    public static final String FILE_MINIO_CHANNEL = "jiraclone";

    public enum DOCUMENT_TYPE {
        TASK(1),
        AVATAR(2),
        COVER(3),
        POST(4);

        public final Integer value;

        DOCUMENT_TYPE(Integer value) {
            this.value = value;
        }
    }

    public enum STATUS {
        ACTIVE(1),
        IN_ACTIVE(0);
        public final int value;

        STATUS(int value) {
            this.value = value;
        }
    }

    public enum STATUS_ISSUE {
        NEW(1, "New"),
        CONFIRMED(2, "Confirmed"),
        DEPLOY_WAITING(3, "Deploy waiting"),
        RESOLVE(4, "Resolve"),
        REOPEN(5, "Reopen"),
        DONE(6, "Done"),
        REJECT(7, "Reject");
        public final int value;
        public final String name;

        STATUS_ISSUE(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    public enum PROFESSIONAL_LEVEL {
        INTERN(1),
        FRESHER(2),
        JUNIOR(3),
        MIDDLE(4),
        SENIOR(5);
        public final int value;

        PROFESSIONAL_LEVEL(int value) {
            this.value = value;
        }
    }

    public enum PERMISSION {
        MEMBER(1),
        LEADER(2),
        PROJECT_MANAGEMENT(3),
        ADMIN(4);
        public final int value;

        PERMISSION(int value) {
            this.value = value;
        }
    }
}
