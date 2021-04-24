package com.project.devidea.modules.community.form;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.community.Comment;
import com.project.devidea.modules.community.CommunityType;
import com.project.devidea.modules.community.validator.Enum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RequestCommunity {

    @NotEmpty(message = "제목을 입력해주세요")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    @Enum(enumClass = CommunityType.class)
    private String communityType;

    @Builder
    public RequestCommunity(String title, String content, String communityType) {
        this.title = title;
        this.content = content;
        this.communityType = communityType;
    }
}
