package com.project.devidea.infra.error;

import com.project.devidea.infra.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<FieldError> errors;
//    에러 발생시 줘야할 데이터가 있을거 같아서 추가했습니다. -범석
    private Object data;


    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.code = code.getCode();
        this.errors = errors;
        this.status = code.getStatus();
        this.message = "";
    }

    private ErrorResponse(final ErrorCode code) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.errors = new ArrayList<>();
        this.message = "";
    }

    private ErrorResponse(final ErrorCode code, String message) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.errors = new ArrayList<>();
        this.message = message;
    }

    public ErrorResponse(ErrorCode code, List<FieldError> errors, String message) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.errors = errors;
        this.message = message;
    }

    public ErrorResponse(ErrorCode code, String message, Object data) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.message = message;
        this.data = data;
    }

    public static ErrorResponse of(final ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code, final Errors errors) {
        return new ErrorResponse(code, FieldError.of(errors));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    public static ErrorResponse of(final ErrorCode code, final Errors errors, String message) {
        return new ErrorResponse(code, FieldError.of(errors), message);
    }

    public static ErrorResponse of(ErrorCode code, String message, Object data) {
        return new ErrorResponse(code, message, data);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(toList());
        }

        private static List<FieldError> of(final Errors errors) {
            List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
            return fieldErrors.stream().map(error -> new FieldError(
                    error.getField(),
                    error.getCode(),
                    error.getDefaultMessage())).collect(toList());
        }
    }
}
