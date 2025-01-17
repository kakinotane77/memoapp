package in.techcamp.memoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(properties = "spring.profiles.active=test")
class MemoAppApplicationTests {

	@Test
	void contextLoads() {
		// テストコード
	}

	@Configuration
	@Profile("test") // テスト環境でのみ適用
	static class TestSecurityConfig {
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}
}