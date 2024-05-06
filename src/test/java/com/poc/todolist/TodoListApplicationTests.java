package com.poc.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Just to test basic Springboot start
 */
@SpringBootTest
@ActiveProfiles("test") // will use src\test\resources\application-test.yml property file
class TodoListApplicationTests
{
	@Test
	void contextLoads() {
	}
}
