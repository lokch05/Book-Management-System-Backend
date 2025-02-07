package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.dto.AuthorInput;
import com.example.bookmanagementsystem.dto.BookInput;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.repository.AuthorRepository;
import com.example.bookmanagementsystem.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;

	@Override
	@DgsQuery
	public List<Book> retrieveBooks(
		@InputArgument String titleFilter
	) {
		if(titleFilter == null)
			return bookRepository.findAll();

		return bookRepository.findByTitleContainingIgnoreCase(titleFilter);
	}

	@Override
	@DgsQuery
	public Optional<Book> retrieveBookById(
		@InputArgument String idFilter
	) {
		if (idFilter == null)
			throw new NullPointerException("Book ID cannot be null!");

		return bookRepository.findById(Integer.parseInt(idFilter));
	}

	@Override
	@DgsMutation
	public Book createBook(
		@InputArgument String title,
		@InputArgument Integer publicationYear
	) {
		if (title == null || title.isBlank())
			throw new IllegalArgumentException("Book title cannot be empty!");

		Book newBook = Book.builder()
			.title(title)
			.publicationYear(publicationYear)
			.build();

		return bookRepository.save(newBook);
	}

	@Override
	@DgsMutation
	public Book updateBookById(
		@InputArgument String id,
		@InputArgument String title,
		@InputArgument Integer publicationYear
	) {
		Optional<Book> ob = bookRepository.findById(Integer.parseInt(id));

		if (ob.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Book with id %s not found!", id));
		}

		Book bookToBeUpdated = ob.get();

		if (title != null && !title.isBlank())
			bookToBeUpdated.setTitle(title);

		bookToBeUpdated.setPublicationYear(publicationYear);

		return bookRepository.save(bookToBeUpdated);
	}

	@Override
	@DgsMutation
	public Book bookAddAuthor(
		@InputArgument String id,
		DataFetchingEnvironment dataFetchingEnvironment
	) {
		Map<String,Object> input = dataFetchingEnvironment.getArgument("author");
		AuthorInput authorInput = new ObjectMapper().convertValue(input, AuthorInput.class);

		if (authorInput == null)
			throw new NullPointerException("Author input cannot be null!");

		Optional<Book> ob = bookRepository.findById(Integer.parseInt(id));

		if (ob.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Book with id %s not found!", id));
		}

		Book book = ob.get();

		Author newAuthor = Author.builder()
			.name(authorInput.name())
			.build();

		book.getAuthors().add(newAuthor);

		return bookRepository.save(book);
	}

	@Override
	@DgsMutation
	public Book bookAddAuthorByAuthorId(
		@InputArgument String id,
		@InputArgument String authorId
	) {
		Optional<Book> ob = bookRepository.findById(Integer.parseInt(id));
		if (ob.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Book with id %s not found!", id));
		}

		Optional<Author> oa = authorRepository.findById(Integer.parseInt(authorId));
		if (oa.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Author with id %s not found!", authorId));
		}

		Book book = ob.get();
		Author author = oa.get();
		book.getAuthors().add(author);

		return bookRepository.save(book);
	}

	@Override
	@DgsMutation
	public Book bookRemoveAuthorByAuthorId(
		@InputArgument String id,
		@InputArgument String authorId
	) {
		Optional<Book> ob = bookRepository.findById(Integer.parseInt(id));
		if (ob.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Book with id %s not found!", id));
		}

		Book book = ob.get();
		List<Author> authors = book.getAuthors()
			.stream().filter(
				(author -> author.getId() != Integer.parseInt(authorId)))
			.toList();
		book.setAuthors(authors);

		return bookRepository.save(book);
	}

	@Override
	@DgsMutation
	public boolean deleteBookById(@InputArgument String id) {
		bookRepository.deleteById(Integer.parseInt(id));

		return bookRepository.findById(Integer.parseInt(id)).isEmpty();
	}
}
