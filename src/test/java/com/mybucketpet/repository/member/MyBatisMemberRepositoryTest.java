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

import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Test
    @DisplayName("닉네임 조회 테스트")
    void findByNickname() {
        // given
        String nickname = "testttt";
        String nickname2 = "test2";
        Member member = new Member("test@gmail.com", "testttttt",
                "test2", "Y", "N", JoinType.EMAIL);
        memberRepository.save(member);
        // when
        Optional<String> findNickname = memberRepository.findByNickname(nickname);
        Optional<String> findNickname2 = memberRepository.findByNickname(nickname2);
        // then
        Assertions.assertThatThrownBy(() -> findNickname.get().equals(nickname)).isInstanceOf(NoSuchElementException.class);
        Assertions.assertThat(findNickname2.get()).isEqualTo(nickname2);
    }
}