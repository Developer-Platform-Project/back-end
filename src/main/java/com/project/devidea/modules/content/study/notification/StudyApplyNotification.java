package com.project.devidea.modules.content.study.notification;

import com.project.devidea.modules.content.study.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public class StudyApplyNotification implements StudyNotification{

    @Override
    public void send(Study study, JpaRepository jpaRepository) {

    }
}