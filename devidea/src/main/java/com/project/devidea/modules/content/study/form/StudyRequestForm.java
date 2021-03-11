package com.project.devidea.modules.content.study.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyRequestForm {

    private String keyword; //keyword

    @NotBlank
    private Long page; //page

    private List<String> tags;

    private Integer pageSize;

    private String localNameOfCity; ////서울특별시

    private String province; //송파구

    private Boolean recruiting; //모집하고 있는 스터디


    private Boolean mentorRecruiting; //멘토 있는 스터디

    private Boolean like; //좋아요 순으로

}
