package com.example.bookmanagementsystem.author;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmanagementsystem.SharedContainerWithReuseTest;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import java.util.List;
import org.junit.jupiter.api.Test;

class AuthorCoreDomainTest extends SharedContainerWithReuseTest {

	@Test
	void shouldBeAbleToInitialiseAuthor() {
		Author author = new Author("J. K. Rowling");

		assertThat(author).isNotNull();
	}

	@Test
	void shouldBeAbleToInitialiseAuthorWithBuilder() {
		Author author = Author.builder()
			.name("J. K. Rowling")
			.build();

		assertThat(author).isNotNull();
	}

	@Test
	void shouldBeAbleToChangeAuthorName() {
		Author author = Author.builder()
			.name("J. K. Rowling")
			.build();

		String newName = "Joanne Rowling";

		author.setName(newName);
		assertThat(author.getName()).isEqualTo(newName);
	}

	@Test
	void shouldBeAbleToAddBook() {
		Author author = Author.builder()
			.name("J. K. Rowling")
			.build();

		Book book = new Book(
			"Harry Potter and the Philosopher’s Stone",
			1997,
			List.of()
		);

		author.setBooks(List.of(book));
		assertThat(author.getBooks()).hasSize(1);
	}

	@Test
	void shouldBeAbleToAddBooks() {
		Author author = Author.builder()
			.name("J. K. Rowling")
			.build();

		Book book = new Book(
			"Harry Potter and the Philosopher’s Stone",
			1997,
			List.of()
		);
		Book book1 = new Book(
			"Harry Potter and the Chamber of Secrets",
			1998,
			List.of()
		);

		author.setBooks(List.of(book, book1));
		assertThat(author.getBooks()).hasSize(2);
	}

}
