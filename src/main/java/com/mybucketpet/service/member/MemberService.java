package com.mybucketpet.service.member;

import com.mybucketpet.controller.join.JoinForm;
import com.mybucketpet.controller.login.PasswordChangeForm;
import com.mybucketpet.domain.member.Member;

import java.util.Optional;

public interface MemberService {
    // 회원 가입
    Member save(JoinForm member);
    // 회원 조회, 필요한 정보만 전달
    Member findById(String memberId);
    // 닉네임 조회
    String findByNickname(String nickName);
    // 회원 로그인 가능 여부 체크
    Member findByLoginAvailability(String memberId, String memberPw);
    // 회원 비밀번호 재설정
    void update(PasswordChangeForm passwordChangeForm);
}
