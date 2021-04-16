package com.project.devidea.modules.content.study.exception;

import com.project.devidea.infra.error.exception.BusinessException;
import com.project.devidea.infra.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyApplyException extends BusinessException {
    static String message="이미 지원하였습니다.";
    public AlreadyApplyException(String message){
        super(message, ErrorCode.ENTITY_ALREADY_EXIST);
        log.error("custom____sadsasdaad_______________");
        System.out.println("custom_fdsafasfadsfasd");
    }

}
