package com.mybucketpet.controller.login.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginForm {

    private String loginId;
    private String loginPw;

    public LoginForm() {}

    public LoginForm(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}
