package com.mybucketpet.controller.join.dto;

import com.mybucketpet.domain.member.JoinType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class JoinForm {
    @NotBlank
    private String joinEmailPrefix;   // 회원 아이디(이메일) 접두사
    @NotBlank
    private String joinEmailSuffix;   // 회원 아이디(이메일) 접미사 (셀렉박스)
    private String joinEmailSuffixSelf; // 회원 아이디(이메일) 접미사 (직접입력)
    @NotBlank
    private String authCode;        // 메일 인증 코드
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$")
    @NotBlank
    private String joinPassword;      // 회원 비밀번호
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$")
    @NotBlank
    private String joinPasswordCheck; // 회원 비밀번호 확인
    @NotBlank
    @Size(min = 2, max = 15)
    private String joinNickname;      // 회원 닉네임
    @AssertTrue
    private boolean termsUse;         // 이용약관 동의여부
    @AssertTrue
    private boolean privacyUse;        // 개인정보수집 동의여부
    private boolean marketingUse;      // 마케팅활용 동의여부
    private JoinType joinPath;          // 가입경로

    public JoinForm() { }

    public JoinForm(JoinType joinPath) {
        this.joinPath = joinPath;
    }

    public JoinForm(String joinEmailPrefix, String joinEmailSuffix, String joinEmailSuffixSelf, String authCode,
                    String joinPassword, String joinPasswordCheck, String joinNickname, boolean termsUse,
                    boolean privacyUse, boolean marketingUse, JoinType joinPath) {
        this.joinEmailPrefix = joinEmailPrefix;
        this.joinEmailSuffix = joinEmailSuffix;
        this.joinEmailSuffixSelf = joinEmailSuffixSelf;
        this.authCode = authCode;
        this.joinPassword = joinPassword;
        this.joinPasswordCheck = joinPasswordCheck;
        this.joinNickname = joinNickname;
        this.termsUse = termsUse;
        this.privacyUse = privacyUse;
        this.marketingUse = marketingUse;
        this.joinPath = joinPath;
    }
}
