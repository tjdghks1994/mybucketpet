package com.mybucketpet.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class JasyptConfigTest {
    private final StringEncryptor encryptor;
    @Autowired
    public JasyptConfigTest(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }
    @Test
    void stringEncryptor() {
        log.info("encryptor = {}", encryptor.getClass());

        String enc = encryptor.encrypt("sa");
        log.info("enc = {}", enc);
    }

}