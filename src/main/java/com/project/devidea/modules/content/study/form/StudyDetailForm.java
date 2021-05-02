package com.project.devidea.modules.content.study.form;

import com.project.devidea.modules.content.study.Study;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter @Setter
@EqualsAndHashCode(callSuper=false)
public class StudyDetailForm extends StudyBaseForm{
    Set<String> members = new HashSet<>();
    String fullDescription;
    boolean open; //공개여부
    String question;

    @Override
    public Study toStudy(){
        Study study=super.toStudy();
        study.setQuestion(question);
        study.setOpen(open);
        study.setFullDescription(fullDescription);
        return study;
    }
}
