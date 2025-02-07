package com.example.bookmanagementsystem;

import static org.assertj.core.api.Assertions.assertThat;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public abstract class SharedContainerWithReuseTest {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.4")
		.withDatabaseName("book_management_system_test")
		.withUsername("root")
		.withPassword("test")
		.withAccessToHost(true)
		.withReuse(true);;

	@BeforeAll
	static void beforeAll() {
		mySQLContainer.start();
	}

	@AfterAll
	public static void afterAll() {
		mySQLContainer.close();
	}

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username=", mySQLContainer::getUsername);
		registry.add("spring.datasource.password=", mySQLContainer::getPassword);
		registry.add("integration-tests-db", mySQLContainer::getDatabaseName);
	}

	@Test
	void containerIsCreated() {
		assertThat(mySQLContainer.isCreated()).isTrue();
	}

	@Test
	void containerIsRunning() {
		assertThat(mySQLContainer.isRunning()).isTrue();
	}

	@Test
	void containerHostIsAccessible() {
		assertThat(mySQLContainer.isHostAccessible()).isTrue();
	}
}
