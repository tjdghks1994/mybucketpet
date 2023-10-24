package com.mybucketpet.repository.member;

import com.mybucketpet.controller.member.dto.LoginForm;
import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import com.mybucketpet.domain.member.MemberType;
import com.mybucketpet.exception.member.NotFoundMemberException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional // @Transactional 이 테스트에 있으면 스프링은 테스트를 트랜잭션 안에서 실행하고, 테스트가 끝나면 트랜잭션을 자동으로 롤백시킨다
@SpringBootTest // @SpringBootApplication 애노테이션을 찾아서 해당 클래스의 설정 정보를 사용한다
class MyBatisMemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원등록 테스트")
    void save() {
        // given
        Member member = Member.builder()
                .memberId("hello@naver.com")
                .memberPw(passwordEncoder.encode("hello1234!!"))
                .memberNickname("HelloWorld")
                .marketingYN("Y")
                .suspendYN("N")
                .joinType(JoinType.EMAIL)
                .memberType(MemberType.NORMAL)
                .build();
        // when
        memberRepository.save(member);
        LoginForm findById = memberRepository.findById(member.getMemberId());
        // then
        Assertions.assertThat(member.getMemberId()).isEqualTo(findById.getLoginId());
        Assertions.assertThat(member.getMemberPw()).isEqualTo(findById.getLoginPw());
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void findMember() {
        // given
        String findId = "testAdmin";
        // when
        LoginForm find = memberRepository.findById(findId);
        // then
        Assertions.assertThat(findId).isEqualTo(find.getLoginId());
    }

    @Test
    @DisplayName("회원 조회 실패 테스트")
    void fail_findMember() {
        // given
        String failId = "aaaa";
        // when then
        Assertions.assertThatThrownBy(() -> memberRepository.findById(failId))
                .isInstanceOf(NotFoundMemberException.class);
    }

//    @Test
//    @DisplayName("회원 중복 조회 테스트")
//    void duplicate_member() {
//        // given
//        String duplicateId = "testAdmin";
//        // when
//        Optional<String> byIdDuplicate = memberRepository.findByIdDuplicate(duplicateId);
//        // then
//        Assertions.assertThat(duplicateId).isEqualTo(byIdDuplicate.get());
//    }
}