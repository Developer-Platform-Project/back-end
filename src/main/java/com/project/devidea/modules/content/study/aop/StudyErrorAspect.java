package com.project.devidea.modules.content.study.aop;

import com.project.devidea.modules.content.study.exception.AlreadyStudyExistsException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
//https://www.baeldung.com/spring-aop-get-advised-method-info 참고
public class StudyErrorAspect {

    @Pointcut("execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.applyStudy(..))")
    public void publicTarget() {

    }

    @AfterThrowing(pointcut = "@annotation(AlreadyExistError)", throwing= "exception")
    public Object afterThrowingAdvice(JoinPoint jp,Exception exception) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        AlreadyExistError alreadyExistError=signature.getMethod().getAnnotation(AlreadyExistError.class);
        throw new AlreadyStudyExistsException(alreadyExistError.message());
//        try {
//           Object object= joinPoint.proceed();
//           return object;
//        }
//        catch (Exception e){
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//            AlreadyExistError alreadyExistError = method.getAnnotation(AlreadyExistError.class);
//            throw new AlreadyApplyException(alreadyExistError.message());
//        }
    }
}
