package com.mybucketpet.service.member;

import com.mybucketpet.controller.member.dto.JoinForm;
import com.mybucketpet.controller.member.dto.ResponseJoinInfo;
import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.exception.member.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;


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
    @DisplayName("회원 등록 테스트")
    void join() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .joinEmail("test@naver.com")
                .joinPassword("test1234!!")
                .joinPasswordCheck("test1234!!")
                .joinNickname("test_hello")
                .marketingUse(true)
                .privacyUse(true)
                .termsUse(true)
                .joinType(JoinType.EMAIL)
                .authCode("192939")
                .build();
        // when
        ResponseJoinInfo saveMember = memberService.save(joinForm);
        // then
        Assertions.assertThat(joinForm.getJoinEmail()).isEqualTo(saveMember.getMemberId());
        Assertions.assertThat(joinForm.getJoinNickname()).isEqualTo(saveMember.getMemberNickname());
        Assertions.assertThat(joinForm.getJoinType()).isEqualTo(saveMember.getJoinType());
    }

    @Test
    @DisplayName("회원 등록 실패 테스트 - 회원 중복 조회 테스트")
    void fail_join() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .joinEmail("testAdmin") // 중복 회원
                .joinPassword("11111111")
                .joinPasswordCheck("11111111")
                .joinNickname("test_hello")
                .marketingUse(true)
                .privacyUse(true)
                .termsUse(true)
                .joinType(JoinType.EMAIL)
                .authCode("192939")
                .build();
        // when then
        Assertions.assertThatThrownBy(() -> memberService.save(joinForm)).isInstanceOf(DuplicateMemberException.class);
    }
}
