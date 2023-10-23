package com.mybucketpet.repository.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;
import com.mybucketpet.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    // 회원 가입
    ResponseJoinInfo save(Member member);
    // 회원 조회
    LoginForm findById(String memberId);
    // 회원 중복 조회
    Optional<String> findByIdDuplicate(String memberId);
    // 닉네임 조회
    Optional<String> findByNickname(String nickname);
    // 회원 비밀번호 재설정
    void update(String memberPw, String memberId);
}
