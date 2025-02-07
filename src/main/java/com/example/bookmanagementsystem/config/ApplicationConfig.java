package com.example.bookmanagementsystem.config;

import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.repository.AuthorRepository;
import com.example.bookmanagementsystem.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	@Bean
	public List<Author> initialiseAuthors() {
		List<Author> authors = List.of(
			new Author(
				"J. K. Rowling"
			),
			new Author(
				"J. R. R. Tolkien"
			),
			new Author(
				"George R. R. Martin"
			)
		);

		return authorRepository.saveAll(authors);
	}

	@Bean
	public List<Book> initialiseBooks() {
		List<Author> rowling = authorRepository.findByName("J. K. Rowling");
		List<Author> martin = authorRepository.findByName("George R. R. Martin");

		List<Book> books = List.of(
			new Book(
				"Harry Potter and the Philosopher’s Stone",
				1997,
				rowling
			),
			new Book(
				"Harry Potter and the Chamber of Secrets",
				1998,
				rowling
			),
			new Book(
				"Harry Potter and the Prisoner of Azkaban",
				1999,
				rowling
			),
			new Book(
				"Harry Potter and the Goblet of Fire",
				2000,
				rowling
			),
			new Book(
				"Harry Potter and the Order of the Phoenix",
				2003,
				rowling
			),
			new Book(
				"Harry Potter and the Half-Blood Prince",
				2005,
				rowling
			),
			new Book(
				"Harry Potter and the Deathly Hallows",
				2007,
				rowling
			),
			new Book(
				"A Game of Thrones",
				1996,
				martin
			),
			new Book(
				"A Clash of Kings",
				1998,
				martin
			),
			new Book(
				"A Storm of Swords",
				2000,
				martin
			),
			new Book(
				"A Feast for Crows",
				2005,
				martin
			),
			new Book(
				"A Dance with Dragons",
				2011,
				martin
			)
		);

		return bookRepository.saveAll(books);
	}
}
