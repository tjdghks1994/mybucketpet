package com.mybucketpet.controller.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginForm {
    @NotEmpty(message = "이메일을 입력해 주세요.")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String loginPw;

    private LoginForm(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }

    public static LoginForm of(String loginId, String loginPw) {
        return new LoginForm(loginId, loginPw);
    }
}
