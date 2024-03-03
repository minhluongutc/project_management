package datn.backend.utils;

import datn.backend.config.core.I18n;
import datn.backend.utils.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);


    /**
     * Return data to client
     *
     * @param itemObject
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(Object itemObject) {
        BaseResponse baseResponse = new BaseResponse();
        if (itemObject != null) {
            baseResponse.setData(itemObject);
        }
        baseResponse.setCode(ErrorApp.SUCCESS.getCode());
        baseResponse.setMessage(I18n.getMessage(ErrorApp.SUCCESS.getDescription()));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    /**
     * Return data to client
     *
     * @param errorApp
     * @param itemObject
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(ErrorApp errorApp, Object itemObject) {
        BaseResponse baseResponse = new BaseResponse();
        if (itemObject != null) {
            baseResponse.setData(itemObject);
        }
        baseResponse.setCode(errorApp.getCode());
        baseResponse.setMessage(I18n.getMessage(errorApp.getDescription()));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    /**
     * Return mess to client
     *
     * @param code
     * @param description
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(int code, String description) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(description);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    /**
     * Return mess to client
     *
     * @param code
     * @param description
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(int code, String description, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(description);
        if (httpStatus == null) httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

    /**
     * Return mess to client
     * @param errorApp
     * @param httpStatus
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(ErrorApp errorApp, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(errorApp.getCode());
        baseResponse.setMessage(errorApp.getDescription());
        if (httpStatus == null) httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(baseResponse, httpStatus);
    }
}
