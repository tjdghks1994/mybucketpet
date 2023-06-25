package com.mybucketpet.repository.member;

import com.mybucketpet.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    // 회원 가입
    void save(Member member);
    // 회원 조회
    Optional<Member> findById(String memberId);
}
