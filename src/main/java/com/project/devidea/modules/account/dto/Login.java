package com.project.devidea.modules.account.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Login {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Common{

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OAuth{

        @NotBlank(message = "소셜 계정 제공자를 입력해주세요.")
        private String provider;

        @NotBlank(message = "id를 입력해주세요.")
        private String id;

        @NotBlank(message = "이메일 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private boolean savedDetail;

        private String emailCheckToken;
    }
}
