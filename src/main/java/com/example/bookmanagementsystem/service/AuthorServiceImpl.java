package com.example.bookmanagementsystem.service;

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
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	@Override
	@DgsQuery
	public List<Author> retrieveAuthors(
		@InputArgument String nameFilter
	) {
		if(nameFilter == null)
			return authorRepository.findAll();

		return authorRepository.findByNameContainingIgnoreCase(nameFilter);
	}

	@Override
	@DgsQuery
	public Optional<Author> retrieveAuthorById(
		@InputArgument String idFilter
	) {
		if (idFilter == null)
			throw new NullPointerException("Author ID cannot be null!");

		return authorRepository.findById(Integer.parseInt(idFilter));
	}

	@Override
	@DgsMutation
	public Author createAuthor(
		@InputArgument String name
	) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("Author name cannot be empty!");

		Author newAuthor = Author.builder().name(name).build();

		return authorRepository.save(newAuthor);
	}

	@Override
	@DgsMutation
	public Author updateAuthorById(
		@InputArgument String id,
		@InputArgument String name
	) {
		Optional<Author> oa = authorRepository.findById(Integer.parseInt(id));

		if (oa.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Author with id %s not found!", id));
		}

		Author authorToBeUpdated = oa.get();

		if (name != null && !name.isBlank())
			authorToBeUpdated.setName(name);

		return authorRepository.save(authorToBeUpdated);
	}

	@Override
	@DgsMutation
	public Author authorAddBook(
		@InputArgument String id,
		DataFetchingEnvironment dataFetchingEnvironment
	) {
		Map<String,Object> input = dataFetchingEnvironment.getArgument("book");
		BookInput bookInput = new ObjectMapper().convertValue(input, BookInput.class);

		if (bookInput == null)
			throw new NullPointerException("Book input cannot be null!");

		Optional<Author> oa = authorRepository.findById(Integer.parseInt(id));

		if (oa.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Author with id %s not found!", id));
		}

		Author author = oa.get();

		Book newBook = Book.builder()
			.title(bookInput.title())
			.publicationYear(bookInput.publicationYear())
			.build();

		author.getBooks().add(newBook);

		return authorRepository.save(author);
	}

	@Override
	@DgsMutation
	public Author authorAddBookByBookId(
		@InputArgument String id,
		@InputArgument String bookId
	) {
		Optional<Author> oa = authorRepository.findById(Integer.parseInt(id));
		if (oa.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Author with id %s not found!", id));
		}

		Optional<Book> ob = bookRepository.findById(Integer.parseInt(bookId));
		if (ob.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Book with id %s not found!", bookId));
		}

		Author author = oa.get();
		Book book = ob.get();
		author.getBooks().add(book);

		return authorRepository.save(author);
	}

	@Override
	@DgsMutation
	public Author authorRemoveBookByBookId(
		@InputArgument String id,
		@InputArgument String bookId
	) {
		Optional<Author> oa = authorRepository.findById(Integer.parseInt(id));
		if (oa.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("Author with id %s not found!", id));
		}

		Author author = oa.get();
		List<Book> books = author.getBooks()
			.stream().filter(
				(book -> book.getId() != Integer.parseInt(bookId)))
			.toList();
		author.setBooks(books);

		return authorRepository.save(author);
	}

	@Override
	@DgsMutation
	public boolean deleteAuthorById(@InputArgument String id) {
		authorRepository.deleteById(Integer.parseInt(id));

		return authorRepository.findById(Integer.parseInt(id)).isEmpty();
	}
}
