package datn.backend.utils.exceptions;//package datn.backend.utils.exceptions;
//
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//import datn.backend.utils.ErrorApp;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.TypeMismatchException;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
////import org.springframework.security.access.AccessDeniedException;
////import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.servlet.NoHandlerFoundException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
////import vn.com.viettel.auth.utils.AuthUtils;
//import vn.com.viettel.core.config.I18n;
//import vn.com.viettel.core.dto.response.BaseResponse;
//import vn.com.viettel.core.utils.HandleExceptionUtils;
////import vn.com.viettel.utils.ErrorApp;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.ConstraintViolationException;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Objects;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> handleValidateException(ConstraintViolationException e, HttpServletRequest request) {
//        LOGGER.error("Has ERROR", e);
//        return HandleExceptionUtils.errorResponse(HandleExceptionUtils.getMsgValidateException(e), request.getRequestURI(), HttpStatus.BAD_REQUEST);
//    }
//
////    @ExceptionHandler(AccessDeniedException.class)
////    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req, Authentication authentication) {
////        LOGGER.error("Has Access is denied ERROR user: {} in: {}", AuthUtils.getUserLogin(authentication), req.getRequestURI());
////        return HandleExceptionUtils.errorResponse(ErrorApp.FORBIDDEN.getDescription(), req.getRequestURI(), HttpStatus.FORBIDDEN);
////    }
//
//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        LOGGER.error("Has ERROR MethodArgumentNotValidException with message = {}", ex.getMessage());
//        return HandleExceptionUtils.errorResponse(HandleExceptionUtils.getMsgMethodArgumentNotValid(ex), null, HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        LOGGER.error("Has ERROR handleHttpMessageNotReadable with message = {}", ex.getMessage());
//        if (ex.getCause() instanceof InvalidFormatException) {
//            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
//            for (JsonMappingException.Reference path : cause.getPath()) {
//                String mess = path.getFieldName() + ": Invalid format";
//                return HandleExceptionUtils.errorResponse(I18n.getMessage(mess), null, HttpStatus.BAD_REQUEST);
//            }
//        }
//        return HandleExceptionUtils.errorResponse(ErrorApp.INTERNAL_SERVER.getDescription(), null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<Object> handleCustomException(CustomException ex, HttpServletRequest request) {
//        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
//            LOGGER.error("Has ERROR CustomException with message = {}, at = {}", ex.getMessage(), ex.getStackTrace()[0].toString());
//        } else {
//            LOGGER.error("Has ERROR CustomException with code = {}, message = {}", ex.getMessage(), ex.getMessage());
//        }
//        if (Objects.isNull(ex.getErrorApp()) && Objects.nonNull(ex.getCodeError())) {
//            return errorResponse(ex.getErrorApp(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
//        }
//        return HandleExceptionUtils.errorResponse(I18n.getMessage(ex.getMessage()), request.getRequestURI(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
//        LOGGER.error("Has ERROR", ex);
//        return HandleExceptionUtils.errorResponse(ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return handleExceptionInternal(ex, null, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        LOGGER.error("No handler found exception", ex);
//        return handleExceptionInternal(ex, null, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        LOGGER.error("Handle type mismatch", ex);
//        return handleExceptionInternal(ex, null, headers, status, request);
//    }
//
//    @ExceptionHandler(IOException.class)
//    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)   //(1)
//    public Object exceptionHandler(IOException e, HttpServletRequest request) {
//        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e), "Broken pipe")) {   //(2)
//            return null;        //(2)	socket is closed, cannot return any response
//        } else {
//            return new HttpEntity<>(e.getMessage());  //(3)
//        }
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException ex) {
//        if (ex.getStackTrace() != null) {
//            LOGGER.error("Has ERROR MethodArgumentTypeMismatchException with message = {}, at = {}",
//                    ex.getMessage(), ex.getStackTrace()[0].toString());
//        } else {
//            LOGGER.error("Has ERROR MethodArgumentTypeMismatchException with message = {}", ex.getMessage());
//        }
//        return HandleExceptionUtils.errorResponse(ErrorApp.BAD_REQUEST_PATH.getDescription(), null, HttpStatus.BAD_REQUEST);
//    }
//
//    public static ResponseEntity<Object> errorResponse(ErrorApp errorApp, String path, HttpStatus httpStatus) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setCode(errorApp.getCode());
//        baseResponse.setMessage(errorApp.getDescription());
//        baseResponse.setPath(path);
//        baseResponse.setTimestamp(new Date(System.currentTimeMillis()));
//        baseResponse.setStatus(httpStatus.value());
//        return new ResponseEntity<>(baseResponse, httpStatus);
//    }
//}
