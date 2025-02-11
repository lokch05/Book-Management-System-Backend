package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.dto.BookInput;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.repository.AuthorRepository;
import com.example.bookmanagementsystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	@Override
	public List<Book> getBooks(String titleFilter) {
		if(titleFilter == null)
			return bookRepository.findAll();

		return bookRepository.findByTitleContainingIgnoreCase(titleFilter);
	}

	@Override
	public Optional<Book> getBookById(Integer id) {
		return bookRepository.findById(id);
	}

	@Override
	public Book createBook(BookInput book) {
		if (book.title().isBlank())
			throw new IllegalArgumentException("Book title cannot be blank!");

		List<Author> authors = findAuthorsByIds(book.authorsId());

		Book newBook = Book.builder()
			.title(book.title())
			.publicationYear(book.publicationYear())
			.authors(authors)
			.build();

		return bookRepository.save(newBook);
	}

	@Override
	public Book updateBookById(BookInput book) {
		if (book.id().isBlank())
			throw new IllegalArgumentException("Book ID cannot be blank!");

		Book bookToBeUpdated =
			bookRepository.findById(Integer.parseInt(book.id()))
			.orElseThrow(() -> new EntityNotFoundException(
				String.format("Book with ID %s not found!", book.id())));

		if (!book.title().isBlank())
			bookToBeUpdated.setTitle(book.title());

		bookToBeUpdated.setPublicationYear(book.publicationYear());

		List<Author> authors = findAuthorsByIds(book.authorsId());
		bookToBeUpdated.setAuthors(authors);

		return bookRepository.save(bookToBeUpdated);
	}

	@Override
	public boolean deleteBookById(Integer id) {
		bookRepository.deleteById(id);

		return bookRepository.findById(id).isEmpty();
	}

	private List<Author> findAuthorsByIds(List<String> authorsId) {
		if (authorsId.isEmpty())
			throw new IllegalArgumentException("Authors ID cannot be empty!");

		List<Integer> intAuthorsId = authorsId.stream().map(Integer::parseInt).toList();

		return authorRepository.findAllById(intAuthorsId);
	}
}
