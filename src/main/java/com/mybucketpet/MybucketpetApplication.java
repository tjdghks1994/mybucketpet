package com.mybucketpet;

import com.mybucketpet.config.MyBatisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Controller와 관련된 영역만 scan 하도록 설정
 * Service와 Repository 관련 빈들은 별도의 Config 파일을 통해 등록 후 Import 하도록 설정
 */

@Import(MyBatisConfig.class)
@SpringBootApplication(scanBasePackages = "com.mybucketpet.controller")
public class MybucketpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybucketpetApplication.class, args);
	}

}
