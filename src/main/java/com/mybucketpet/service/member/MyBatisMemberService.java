package com.mybucketpet.service.member;

import com.mybucketpet.controller.join.JoinForm;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyBatisMemberService implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member save(JoinForm member) {
        String memberId = "";
        // 이메일 접미사를 직접 입력한 경우
        if (StringUtils.hasText(member.getJoinEmailSuffixSelf())) {
            memberId = member.getJoinEmailPrefix() + "@" + member.getJoinEmailSuffix();
        } else {
            memberId = member.getJoinEmailPrefix() + "@" + member.getJoinEmailSuffix();
        }
        // 비밀번호 암호화 처리
        String encPassword = passwordEncoder.encode(member.getJoinPassword());
        String marketingYN = member.isMarketingUse() ? "Y" : "N";

        Member saveMember = memberRepository.save(
                new Member(memberId, encPassword, member.getJoinNickname(), marketingYN, "N", member.getJoinPath())
        );

        return saveMember;
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member findBySameId(String memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        // 이미 존재하는 회원
        if (findMember.isPresent()) {
            // 필요한 정보만 남기고 나머지는 삭제처리
            // 필요 정보 - ID, JoinType
            Member member = findMember.get();
            member.setMemberPw("");
            member.setMemberNickname("");
            member.setMarketingYN("");
            member.setSuspendYN("");

            return member;
        }
        // 존재하지 않는 회원이라면 빈 객체 전달
        return findMember.orElse(new Member());
    }

    @Override
    public String findByNickname(String nickName) {
        Optional<String> findNickname = memberRepository.findByNickname(nickName);
        // 이미 존재하는 닉네임
        if (findNickname.isPresent()) {
            return nickName;
        }
        return "";
    }

}
