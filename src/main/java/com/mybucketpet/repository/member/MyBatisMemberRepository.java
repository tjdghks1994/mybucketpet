package com.mybucketpet.repository.member;

import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.exception.member.NotFoundMemberException;
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
    public ResponseJoinInfo save(Member member) {
        memberMapper.save(member);
        return ResponseJoinInfo.builder().member(member).build();
    }

    @Override
    public LoginForm findById(String memberId) {
        Member findMember = memberMapper.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));

        return LoginForm.of(findMember.getMemberId(), findMember.getMemberPw());
    }

    @Override
    public Optional<String> findByIdDuplicate(String memberId) {
        return memberMapper.findByIdDuplicate(memberId);
    }

    @Override
    public Optional<String> findByNickname(String nickname) {
        return memberMapper.findByNickname(nickname);
    }

    @Override
    public void update(String memberPw, String memberId) {
        memberMapper.update(memberPw, memberId);
    }
}
