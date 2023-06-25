package com.mybucketpet.config;

import com.mybucketpet.repository.member.MemberMapper;
import com.mybucketpet.repository.member.MemberRepository;
import com.mybucketpet.repository.member.MyBatisMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {
    private final MemberMapper memberMapper;

    @Bean
    public MemberRepository memberRepository() {
        return new MyBatisMemberRepository(memberMapper);
    }
}
