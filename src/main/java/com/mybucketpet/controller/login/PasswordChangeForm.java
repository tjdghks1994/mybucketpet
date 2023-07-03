package com.mybucketpet.controller.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PasswordChangeForm {
    @NotBlank
    private String joinEmailPrefix;   // 회원 아이디(이메일) 접두사
    @NotBlank
    private String joinEmailSuffix;   // 회원 아이디(이메일) 접미사 (셀렉박스)
    private String joinEmailSuffixSelf; // 회원 아이디(이메일) 접미사 (직접입력)
    @NotBlank
    private String authCode;        // 메일 인증 코드
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$")
    @NotBlank
    private String changePassword;      // 회원 변경 비밀번호
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$")
    @NotBlank
    private String changePasswordCheck; // 회원 변경 비밀번호 확인

    public PasswordChangeForm() {}

    public PasswordChangeForm(String joinEmailPrefix, String joinEmailSuffix, String joinEmailSuffixSelf,
                              String authCode, String changePassword, String changePasswordCheck) {
        this.joinEmailPrefix = joinEmailPrefix;
        this.joinEmailSuffix = joinEmailSuffix;
        this.joinEmailSuffixSelf = joinEmailSuffixSelf;
        this.authCode = authCode;
        this.changePassword = changePassword;
        this.changePasswordCheck = changePasswordCheck;
    }
}
