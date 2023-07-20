package com.mybucketpet;

import com.mybucketpet.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Controller와 관련된 영역만 scan 하도록 설정
 * Service와 Repository 관련 빈들 및 그 외의 스프링 빈으로 등록하기 위한 것은 별도의 Config 파일을 통해 등록 후 Import 하도록 설정
 * Spring Security의 PasswordEncoder만 활용하기 위해서 자동으로 설정되는 Security 설정들 제외하도록 exclude 설정
 */

@Import({JasyptConfig.class, MailConfig.class, AppConfig.class, SecurityConfig.class, WebConfig.class})
@SpringBootApplication(scanBasePackages = "com.mybucketpet.controller", exclude = SecurityAutoConfiguration.class)
public class MybucketpetApplication {
	public static void main(String[] args) {
		SpringApplication.run(MybucketpetApplication.class, args);
	}

}
