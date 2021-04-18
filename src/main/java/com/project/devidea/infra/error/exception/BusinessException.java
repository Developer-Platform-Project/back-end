package com.project.devidea.infra.error.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;
    private Errors errors;
    //    에러 발생시 줘야할 데이터가 있을거 같아서 추가했습니다. -범석
    private Object data;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode, Errors errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public BusinessException(ErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Errors getErrors() {
        return errors;
    }

    public Object getData() {
        return data;
    }
}
