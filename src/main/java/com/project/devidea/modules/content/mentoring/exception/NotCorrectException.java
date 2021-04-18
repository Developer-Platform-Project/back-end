package com.project.devidea.modules.content.mentoring.exception;

import com.project.devidea.infra.error.exception.BusinessException;
import com.project.devidea.infra.error.exception.ErrorCode;

public class NotCorrectException extends BusinessException {

    public NotCorrectException() {
        super("데이터가 일치하지 않습니다.", ErrorCode.ENTITY_NOT_CORRECT);
    }

    public NotCorrectException(String message) {
        super(message, ErrorCode.ENTITY_NOT_CORRECT);
    }

}
