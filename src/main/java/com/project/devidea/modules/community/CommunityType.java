package com.project.devidea.modules.community;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.project.devidea.infra.error.exception.ErrorCode;
import org.hibernate.hql.internal.ast.ErrorTracker;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.Embeddable;

public enum CommunityType {
    QA("QA"),
    FREE("FREE");

    String value;
    CommunityType(String value) { this.value = value; }
    public String value() { return value; }

}
