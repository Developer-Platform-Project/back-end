package com.project.devidea.modules.account.exception;

import com.project.devidea.infra.error.exception.BusinessException;
import com.project.devidea.infra.error.exception.ErrorCode;
import com.project.devidea.modules.account.dto.Login;
import lombok.Getter;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.Errors;

@Getter
public class AccountException extends BusinessException {

    public AccountException(ErrorCode errorCode, Errors errors) {
        super("회원 관련 서비스에서 발생한 에러입니다.", errorCode, errors);
    }

    public AccountException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AccountException(ErrorCode errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
