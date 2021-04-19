package com.project.devidea.modules.content.study.exception;

import com.project.devidea.infra.error.exception.BusinessException;
import com.project.devidea.infra.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AlreadyStudyExistsException extends BusinessException {
    static String STUDY_APPLY_MESSAGE="이미 지원하였습니다.";
    static String STUDY_MEMBER="이미 스터디원입니다.";
    static String STUDY_EXISTS="이미 만든 스터디입니다.";
    HashMap<String,String> messageMap;
    public AlreadyStudyExistsException(String message){
        super(message, ErrorCode.ENTITY_ALREADY_EXIST);
    }

}
