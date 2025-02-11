package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.dataloader.AuthorsDataLoader;
import com.example.bookmanagementsystem.dto.AuthorInput;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.repository.AuthorRepository;
import com.example.bookmanagementsystem.repository.BookRepository;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	@Override
	public List<Author> getAuthors(String nameFilter) {
		if(nameFilter.isBlank())
			return authorRepository.findAll();

		return authorRepository.findByNameContainingIgnoreCase(nameFilter.strip());
	}

	public Optional<Author> getAuthorById(Integer authorId) {
		return authorRepository.findById(authorId);
	}

	public List<Author> getAuthorsByIds(List<Integer> authorsId) {
		return authorRepository.findAllById(authorsId);
	}


	@Override
	public Author createAuthor(AuthorInput author) {
		List<Book> books = author.booksId() == null || author.booksId().isEmpty() ?
			List.of() :
			findBooksByIds(author.booksId());

		Author newAuthor = Author.builder()
			.name(author.name())
			.books(books)
			.build();

		return authorRepository.save(newAuthor);
	}

	@Override
	public Author updateAuthorById(AuthorInput author) {
		Author authorToBeUpdated = authorRepository.findById(Integer.parseInt(author.id()))
			.orElseThrow(() -> new EntityNotFoundException(
				String.format("Author with id %s not found!", author.id())
			));

		authorToBeUpdated.setName(author.name().strip());

		List<Book> books = findBooksByIds(author.booksId());
		authorToBeUpdated.setBooks(books);

		return authorRepository.save(authorToBeUpdated);
	}

	@Override
	public boolean deleteAuthorById(Integer id) {
		authorRepository.deleteById(id);

		return authorRepository.findById(id).isEmpty();
	}

	private List<Book> findBooksByIds(List<String> booksId) {
		if(booksId == null || booksId.isEmpty())
			return List.of();

		List<Integer> intBooksId = booksId.stream().map((Integer::parseInt)).toList();

		return bookRepository.findAllById(intBooksId);
	}
}
