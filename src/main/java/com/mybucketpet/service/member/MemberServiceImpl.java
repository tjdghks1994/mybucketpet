package com.mybucketpet.service.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.PasswordChangeForm;
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
public class MemberServiceImpl implements MemberService {
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
    public Member findById(String memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        // 이미 존재하는 회원
        if (findMember.isPresent()) {
            // 중요 정보인 pw는 삭제 처리
            Member member = findMember.get();
            member.setMemberPw("");

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

    @Override
    public Member findByLoginAvailability(String loginId, String loginPw) {
        Optional<Member> findLoginMember = memberRepository.findById(loginId);

        // 입력된 Id 멤버가 조회 되는 경우
        if (findLoginMember.isPresent()) {
            Member member = findLoginMember.get();
            // passwordEncoder를 활용해서 평문과 저장되어있는 암호문이랑 비교
            boolean matches = passwordEncoder.matches(loginPw, member.getMemberPw());
            // 2개의 값이 동일하다고 판단되는 경우 조회한 Member 객체 반환
            if (matches) {
                return member;
            }
        }
        // 조회된 멤버가 없다면 로그인 실패
        // 빈 객체 멤버 반환
        return new Member();
    }

    @Override
    public void update(PasswordChangeForm passwordChangeForm) {
        // 평문을 암호화
        String encPassword = passwordEncoder.encode(passwordChangeForm.getChangePassword());
        String memberId = "";
        // 이메일 접미사를 직접 입력한 경우
        if (StringUtils.hasText(passwordChangeForm.getJoinEmailSuffixSelf())) {
            memberId = passwordChangeForm.getJoinEmailPrefix() + "@" + passwordChangeForm.getJoinEmailSuffix();
        } else {
            memberId = passwordChangeForm.getJoinEmailPrefix() + "@" + passwordChangeForm.getJoinEmailSuffix();
        }
        // 암호화한 패스워드로 수정
        memberRepository.update(encPassword, memberId);
    }

}
