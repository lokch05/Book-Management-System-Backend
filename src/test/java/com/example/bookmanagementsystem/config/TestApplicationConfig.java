package com.example.bookmanagementsystem.config;

import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestApplicationConfig {

	@Bean
	public List<Author> initialiseAuthors() {
		return List.of();
	}

	@Bean
	public List<Book> initialiseBooks() {
		return List.of();
	}
}
