package com.mybucketpet.repository.member;

import com.mybucketpet.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class MyBatisMemberRepository implements MemberRepository {
    private final MemberMapper memberMapper;

    public MyBatisMemberRepository(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public Member save(Member member) {
        memberMapper.save(member);
        return member;
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public Optional<String> findByNickname(String nickname) {
        return memberMapper.findByNickname(nickname);
    }
}
