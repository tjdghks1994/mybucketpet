package com.mybucketpet.service.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.controller.member.dto.PasswordChangeForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.domain.member.MemberType;
import com.mybucketpet.exception.member.DuplicateMemberException;
import com.mybucketpet.exception.member.NotMatchPasswordException;
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
    public ResponseJoinInfo save(JoinForm joinForm) {
        // 중복 회원 조회
        Optional<String> byIdDuplicate = memberRepository.findByIdDuplicate(joinForm.getJoinEmail());
        if (byIdDuplicate.isPresent()) {
            throw new DuplicateMemberException("이미 가입된 회원 ID 입니다.");
        }
        // 비밀번호 암호화 처리
        String encPassword = passwordEncoder.encode(joinForm.getJoinPassword());
        String marketingYN = joinForm.isMarketingUse() ? "Y" : "N";

        Member saveMember = Member.builder()
                .memberId(joinForm.getJoinEmail())
                .memberPw(encPassword)
                .memberNickname(joinForm.getJoinNickname())
                .marketingYN(marketingYN)
                .suspendYN("N")
                .joinType(joinForm.getJoinType())
                .memberType(MemberType.NORMAL)
                .build();

        return memberRepository.save(saveMember);
    }

    @Override
    public void findByIdDuplicate(String memberId) {
        Optional<String> byIdDuplicate = memberRepository.findByIdDuplicate(memberId);
        // 이미 존재하는 회원 ID
        if (byIdDuplicate.isPresent()) {
            throw new DuplicateMemberException("이미 가입된 회원 ID 입니다.");
        }
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
    public LoginForm findByLoginAvailability(String loginId, String loginPw) {
        LoginForm findLoginMember = memberRepository.findById(loginId);

        // passwordEncoder를 활용해서 평문과 저장되어있는 암호문이랑 비교
        boolean matches = passwordEncoder.matches(loginPw, findLoginMember.getLoginPw());
        // 2개의 값이 동일하다고 판단되는 경우 로그인 성공
        if (matches) {
            return findLoginMember;
        } else {
            // 로그인 실패
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
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
