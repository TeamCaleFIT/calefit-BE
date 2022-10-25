package com.calefit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.config.location=" +
	"classpath:/application-local.yml")
class CalefitApplicationTests {

	@Test
	void contextLoads() {
	}

}
