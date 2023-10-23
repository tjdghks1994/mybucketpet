package com.mybucketpet.repository.member;

import com.mybucketpet.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    // 회원 가입
    void save(Member member);
    // 회원 조회
    Optional<Member> findById(String memberId);
    // 회원 중복 조회
    Optional<String> findByIdDuplicate(String memberId);
    // 닉네임 조회
    Optional<String> findByNickname(String nickname);
    // 회원 비밀번호 재설정
    void update(@Param("memberPw") String memberPw,@Param("memberId") String memberId);
}
