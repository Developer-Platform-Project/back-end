package com.project.devidea.modules.content.study.exception;

import com.project.devidea.infra.error.exception.BusinessException;
import com.project.devidea.infra.error.exception.ErrorCode;

import java.util.function.Supplier;

public class StudyNullException extends BusinessException {

    public StudyNullException(String target) {
        super(target + "스터디는 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND);
    }
    public StudyNullException() {
        super("해당 스터디는 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND);
    }
}