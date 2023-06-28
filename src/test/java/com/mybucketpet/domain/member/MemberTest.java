package com.mybucketpet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void equals() {
        // given
        Member member = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member.setMemberId("test@gmail.com");
        member2.setMemberId("test@gmail.com");
        member3.setMemberId("test2@naver.com");
        
        // when
        boolean sameMember = member.equals(member2);
        boolean diffMember = member.equals(member3);

        // then
        Assertions.assertThat(sameMember).isTrue();
        Assertions.assertThat(diffMember).isFalse();
    }

    @Test
    void hasCode() {
        // given
        Member member = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member.setMemberId("test@gmail.com");
        member2.setMemberId("test@gmail.com");
        member3.setMemberId("test2@naver.com");

        // when
        boolean sameMember = member.hashCode() == member2.hashCode();
        boolean diffMember = member.hashCode() == member3.hashCode();

        // then
        Assertions.assertThat(sameMember).isTrue();
        Assertions.assertThat(diffMember).isFalse();
    }

}