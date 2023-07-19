package com.mybucketpet.service.member;

import com.mybucketpet.controller.join.JoinForm;
import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
@Transactional
class MemberServiceImplTest {
    private final MemberService memberService;

    @Autowired
    public MemberServiceImplTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    @DisplayName("회원 등록, 조회 테스트")
    void update() {
        // given
        JoinForm joinForm = new JoinForm("test", "naver.com", "",
                "1q2w3e4", "test1234", "test1234", "tttt",
                true, true, true, JoinType.EMAIL);

        // when
        Member saveMember = memberService.save(joinForm); // 회원 등록
        // 회원 조회
        Member findMember = memberService.findById(joinForm.getJoinEmailPrefix() + "@" + joinForm.getJoinEmailSuffix());
        // then
        Assertions.assertThat(saveMember.getMemberId()).isEqualTo(findMember.getMemberId());
    }
}
