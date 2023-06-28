package com.mybucketpet.repository.member;

import com.mybucketpet.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    // 회원 가입
    Member save(Member member);

    // 회원 조회
    Optional<Member> findById(String memberId);

}
