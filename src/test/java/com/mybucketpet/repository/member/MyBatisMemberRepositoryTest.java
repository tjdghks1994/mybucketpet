package com.mybucketpet.repository.member;

import com.mybucketpet.domain.member.JoinType;
import com.mybucketpet.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional // @Transactional 이 테스트에 있으면 스프링은 테스트를 트랜잭션 안에서 실행하고, 테스트가 끝나면 트랜잭션을 자동으로 롤백시킨다
@SpringBootTest // @SpringBootApplication 애노테이션을 찾아서 해당 클래스의 설정 정보를 사용한다
class MyBatisMemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 및 회원조회 테스트")
    void saveAndFindById() {
        // given
        Member member = new Member("test@naver.com", "1234", "nickname", "y", "n", JoinType.EMAIL);
        // when
        Member saveMember = memberRepository.save(member);
        // then
        Member findMember = memberRepository.findById(member.getMemberId()).get();
        Assertions.assertThat(findMember).isEqualTo(saveMember);
    }


}