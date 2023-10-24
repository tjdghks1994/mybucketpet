package com.mybucketpet.service.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.controller.member.dto.PasswordChangeForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;

public interface MemberService {
    // 회원 가입
    ResponseJoinInfo save(JoinForm member);
    // 회원 중복 조회
    void findByIdDuplicate(String memberId);
    // 닉네임 조회
    String findByNickname(String nickName);
    // 회원 로그인
    LoginForm findByLoginAvailability(String memberId, String memberPw);
    // 회원 비밀번호 재설정
    void update(PasswordChangeForm passwordChangeForm);
}
