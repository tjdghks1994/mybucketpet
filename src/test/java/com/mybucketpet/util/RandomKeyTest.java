package com.mybucketpet.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RandomKeyTest {
    @Test
    void createKey() {
        String key = RandomKey.createKey();
        log.info("key = {}", key);
    }
}