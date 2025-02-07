package com.example.bookmanagementsystem.book;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookCoreDomainTest {

	@Test
	void shouldBeAbleToInitialiseBook() {
		Book book = new Book(
			"A Game of Thrones",
			1996,
			List.of()
		);

		assertThat(book).isNotNull();
	}

	@Test
	void shouldBeAbleToInitialiseBookWithBuilder() {
		Book book = Book.builder()
			.title("A Game of Thrones")
			.publicationYear(1996)
			.build();

		assertThat(book).isNotNull();
	}

	@Test
	void shouldBeAbleToChangeBookTitle() {
		Book book = Book.builder()
			.title("A Game of Thrones")
			.publicationYear(1996)
			.build();

		String newTitle = "A Song of Ice and Fire";

		book.setTitle(newTitle);
		assertThat(book.getTitle()).isEqualTo(newTitle);
	}

	@Test
	void shouldBeAbleToChangeBookPublicationYear() {
		Book book = Book.builder()
			.title("A Game of Thrones")
			.publicationYear(1996)
			.build();

		int newPublicationYear = 1997;

		book.setPublicationYear(newPublicationYear);
		assertThat(book.getPublicationYear()).isEqualTo(newPublicationYear);
	}

	@Test
	void shouldBeAbleToAddAuthor() {
		Book book = Book.builder()
			.title("A Game of Thrones")
			.publicationYear(1996)
			.build();

		Author author = new Author("George R. R. Martin");

		book.setAuthors(List.of(author));
		assertThat(book.getAuthors()).hasSize(1);
	}

	@Test
	void shouldBeAbleToAddAuthors() {
		Book book = Book.builder()
			.title("University Physics")
			.publicationYear(2019)
			.build();

		Author young = new Author("Hugh Young");
		Author freedman = new Author("Roger Freedman");

		book.setAuthors(List.of(young, freedman));
		assertThat(book.getAuthors()).hasSize(2);
	}


}
