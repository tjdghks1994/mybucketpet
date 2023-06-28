package com.mybucketpet.config;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SecurityConfigTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestSecurityConfig.class);
    @Test
    @DisplayName("단방향 암호화 테스트")
    void passwordEnc() {
        // given
        PasswordEncoder encoder = ac.getBean("passwordEncoder", PasswordEncoder.class);
        String rawPw = "1q3km3";

        // when
        String encPw = encoder.encode(rawPw);
        log.info("encPw = {}", encPw);

        // then
        Assertions.assertThat(rawPw).isNotEqualTo(encPw);
        Assertions.assertThat(encoder.matches(rawPw, encPw)).isTrue();

    }

}

@Configuration
class TestSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}