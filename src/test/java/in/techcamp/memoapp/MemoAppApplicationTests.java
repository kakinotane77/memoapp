package in.techcamp.memoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestSecurityConfig.class})
@SpringBootTest
class MemoAppApplicationTests {

	@Test
	void contextLoads() {
		// Application Context が正しくロードされるかを確認
	}
}