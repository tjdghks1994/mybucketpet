package com.mybucketpet.controller.member.dto;

import com.mybucketpet.domain.member.JoinType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinForm {
    @Email(message = "이메일 형식이 올바르지 않습니다.")  // 이메일 validation - 빈 문자열("")과 null 값을 허용됨
    @NotEmpty(message = "이메일을 입력해 주세요.")
    private String joinEmail;   // 회원 아이디(이메일)
    @NotBlank(message = "인증이 완료되지 않았습니다.")
    private String authCode;        // 메일 인증 코드
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$",
            message = "비밀번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String joinPassword;      // 회원 비밀번호
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()-_+|=]{8,20}$",
            message = "비밀번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "비밀번호 확인란을 입력해주세요.")
    private String joinPasswordCheck; // 회원 비밀번호 확인
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 15, message = "닉네임 형식이 올바르지 않습니다.")
    private String joinNickname;      // 회원 닉네임
    @AssertTrue(message = "이용약관을 동의해주세요.")
    private boolean termsUse;         // 이용약관 동의여부
    @AssertTrue(message = "개인정보수집에 동의해주세요.")
    private boolean privacyUse;        // 개인정보수집 동의여부
    private boolean marketingUse;      // 마케팅활용 동의여부
    private JoinType joinType;          // 가입경로

}
