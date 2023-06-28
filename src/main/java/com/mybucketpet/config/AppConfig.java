package com.mybucketpet.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Service와 Repository 관련 빈들을 스캔하는 설정파일
 * */
@Configuration
@ComponentScan(value = {"com.mybucketpet.service", "com.mybucketpet.repository"})
public class AppConfig {
}
