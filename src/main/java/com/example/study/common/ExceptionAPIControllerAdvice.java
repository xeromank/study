package com.example.study.common;

import static com.example.study.common.utils.ApiUtils.newResponse;

import com.example.study.common.enums.ResultCode;
import com.mchange.rmi.NotAuthorizedException;
import jakarta.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@CommonsLog
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAPIControllerAdvice {


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> exception(Exception e) {
        String errorMessage = "Unknown Error.";
        log.error(errorMessage, e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> exception(ResponseStatusException e) {
        return newResponse(e, e.getStatusCode());
    }


    @ExceptionHandler({HttpClientErrorException.class, ConstraintViolationException.class,
        MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<?> badRequest(Exception e) {
        log.error("Bad Request.", e);
        return newResponse(ResultCode.NONE, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> messageNotReadable(HttpMessageNotReadableException e) {
        log.error("Bad Request.", e);
        return newResponse(ResultCode.NONE, e.getMostSpecificCause().getMessage(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> methodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        log.error("Bad Request.", e);
        String errorMessage =
            String.format("해당 파라미터의 형식이 일치하지 않습니다.[%s]", e.getParameter().getParameterName());
        return newResponse(ResultCode.INVALID_PARAM, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidation(
        MethodArgumentNotValidException e) {
        log.error("Bad Request.", e);
        String errorMessage =
            String.format("[%s] : %s", e.getBindingResult().getFieldError().getField(),
                e.getBindingResult().getFieldError().getDefaultMessage());
        return newResponse(ResultCode.INVALID_PARAM, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> dataIntegrityViolation(
        DataIntegrityViolationException e) {
        log.error("DB Error.", e);

        if (e.getRootCause() instanceof SQLException) {
            SQLException sqlException = (SQLException) e.getRootCause();

            if (Objects.equals(sqlException.getSQLState(), "23000")) {
                switch (sqlException.getErrorCode()) {
                    case 1062:
                        return handleSqlDuplicateException(sqlException);
                }
            }

        }

        return newResponse(ResultCode.NONE, e.getRootCause().getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    private static class SqlDupExceptionMessage {

        private String data;
        private String uk;

        public SqlDupExceptionMessage(String message) {
            Pattern p = Pattern.compile("['](.*?)[']");
            Matcher m = p.matcher(message);

            int cnt = 0;
            while (m.find()) {
                String g = m.group(1);
                if (cnt == 0) {
                    data = g;
                } else {
                    uk = g;
                    break;
                }
                cnt++;
            }
        }
    }

    private ResponseEntity<?> handleSqlDuplicateException(SQLException sqlException) {

        SqlDupExceptionMessage sqlDupExceptionMessage = new SqlDupExceptionMessage(
            sqlException.getMessage());

        ResponseEntity responseEntity = null;

        switch (sqlDupExceptionMessage.getUk()) {
            case "UK_33uo7vet9c79ydfuwg1w848f":
                responseEntity = newResponse(ResultCode.NONE, "이미 사용중인 이메일 입니다.",
                    HttpStatus.CONFLICT);
                break;
            case "UKgqfxifkjl1u8x2o93vcc6wemt":
                responseEntity = newResponse(ResultCode.NONE, "이미 예약한 이벤트 입니다.",
                    HttpStatus.CONFLICT);
                break;
            case "UK_pl4047a5k5enw6up4sjs8lyut":
                responseEntity = newResponse(ResultCode.NONE, "이미 사용중인 닉네임 입니다.",
                    HttpStatus.CONFLICT);
                break;
            case "UK39r9ud0s11syrdrp7ocok0gm5":
                responseEntity = newResponse(ResultCode.NONE,
                    "이미 사용중인 연락처 입니다.",
                    HttpStatus.CONFLICT);
                break;
            case "UK_75wp667qkopvye5yx38cuomft":
                responseEntity = newResponse(ResultCode.NONE, "이미 가입된 사업자 입니다.",
                    HttpStatus.CONFLICT);
                break;
            case "UKdajmhqdvcyc2bwls15jmfvu9y":
                responseEntity = newResponse(ResultCode.NONE, sqlDupExceptionMessage.getData(),
                    HttpStatus.CONFLICT);
                break;
            default:
                responseEntity = newResponse(ResultCode.NONE,
                    "중복된 데이터가 존재합니다.(" + sqlDupExceptionMessage.getData() + ")",
                    HttpStatus.CONFLICT);
                break;
        }

        return responseEntity;
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<?> entityNotFound(Exception e) {
        log.error("Not Found.", e);
        return newResponse(ResultCode.NONE, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> forbidden(Exception e) {
        log.error("Forbidden.", e);
        return newResponse(ResultCode.NONE, e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<?> unAuthorized(Exception e) {
        log.error("UnAuthorized.", e);
        return newResponse(ResultCode.NONE, e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> validationBindException(
        BindException e) {
        log.error("Bad Request.", e);
        String errorMessage =
            String.format("[%s] : %s", e.getBindingResult().getFieldError().getField(),
                e.getBindingResult().getFieldError().getDefaultMessage());
        return newResponse(ResultCode.NONE, errorMessage, HttpStatus.BAD_REQUEST);
    }

}
