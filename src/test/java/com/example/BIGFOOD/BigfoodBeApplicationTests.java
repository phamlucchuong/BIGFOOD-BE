package com.example.BIGFOOD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class BigfoodBeApplicationTests {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	void contextLoads() {
		System.out.println(passwordEncoder.encode("123456"));
	}

}
