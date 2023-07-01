package com.mybucketpet.service.member;

import com.mybucketpet.controller.join.JoinForm;
import com.mybucketpet.domain.member.Member;

import java.util.Optional;

public interface MemberService {
    // 회원 가입
    Member save(JoinForm member);
    // 회원 조회
    Optional<Member> findById(String memberId);
    // 이미 존재하는 회원인지 체크 후, 필요한 정보만 전달
    Member findBySameId(String memberId);
    // 닉네임 조회
    String findByNickname(String nickName);
}
