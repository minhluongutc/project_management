package datn.backend.utils;

public class Constants {
    public static final String PUBLIC_REQUEST_MAPPING_PREFIX = "/public/api";
    public static final String REQUEST_MAPPING_PREFIX = "/api";
    public static final String VERSION_API_V1 = "/v1";
    public static final String GET_CONTENT_LINK_URL = REQUEST_MAPPING_PREFIX + VERSION_API_V1 + "/files/content";
    public static final String GET_CONTENT_LINK_URL_PUBLIC = PUBLIC_REQUEST_MAPPING_PREFIX + VERSION_API_V1 + "/files/content";
    public static final String LOCALE_VN = "vi_VN";
    public static final String TIMEZONE_VN = "Asia/Ho_Chi_Minh";
    public static final String DATE_STRING_FORMAT = "dd/MM/yyyy";

    public static final String FILE_MINIO_TENANT = "jiraclone";
    public static final String FILE_MINIO_CHANNEL = "MinhLuong";

    public enum DOCUMENT_TYPE {
        TASK(1);

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

    public enum CHANEG_REQ_STATUS {
        SUGGEST_NOT_VIEW(1),
        SUGGEST_VIEW(2),
        APPROVE(3),
        DENY(4);
        public final int value;

        CHANEG_REQ_STATUS(int value) {
            this.value = value;
        }
    }

    public enum CHART_DAILY_WORK {
        PROPORTION("proportion"),
        WORKLOAD("workload");
        public final String value;

        CHART_DAILY_WORK(String value) {
            this.value = value;
        }
    }

    public enum FILE_TYPE {
        EXCEL("excel"),
        DOC("doc"),
        PDF("pdf");

        public final String value;

        FILE_TYPE(String value) {
            this.value = value;
        }

    }

    public enum SURVEY_TYPE {
        GRAVITY_MAGNETIC_SURVEY(22);

        public final Integer value;

        SURVEY_TYPE(Integer value) {
            this.value = value;
        }

    }

    public enum TEMPLATE_IMPORT {
        GRAVITY_MAGNETIC_DAILY_TASK("GRAVITY_MAGNETIC_DAILY_TASK");
        public final String value;

        TEMPLATE_IMPORT(String value) {
            this.value = value;
        }
    }
}
