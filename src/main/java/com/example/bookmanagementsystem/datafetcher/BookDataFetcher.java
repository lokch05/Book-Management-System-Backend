package com.example.bookmanagementsystem.datafetcher;

import com.example.bookmanagementsystem.dataloader.AuthorsDataLoader;
import com.example.bookmanagementsystem.dataloader.BooksDataLoader;
import com.example.bookmanagementsystem.dto.BookInput;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.service.BookService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	private final BookService bookService;

	@DgsData(parentType = "Author")
	public CompletableFuture<Book> book(DgsDataFetchingEnvironment dfe) {
		DataLoader<String, Book> dataLoader = dfe.getDataLoader(BooksDataLoader.class);
		String id = dfe.getArgument("id");

		return dataLoader.load(id);
	}

	@DgsQuery
	public List<Book> getBooks(@InputArgument String titleFilter) {
		return bookService.getBooks(titleFilter);
	}

	@DgsQuery
	public Optional<Book> getBookById(@InputArgument String id) {
		if (id.isBlank()) {
			throw new NullPointerException("Book ID cannot be blank!");
		}

		return bookService.getBookById(Integer.parseInt(id));
	}

	@DgsMutation
	public Book createBook(@InputArgument BookInput book) {
		if (book.title().isBlank()) {
			throw new IllegalArgumentException("Book title cannot be blank!");
		}

		if (book.authorsId().isEmpty()) {
			throw new IllegalArgumentException("Book must have at least one author!");
		}

		return bookService.createBook(book);
	}

	@DgsMutation
	public Book updateBookById(@InputArgument BookInput book) {
		if (book.id().isBlank()) {
			throw new IllegalArgumentException("Book ID cannot be blank!");
		}

		if (book.title().isBlank()) {
			throw new IllegalArgumentException("Book title cannot be blank!");
		}

		if (book.authorsId().isEmpty()) {
			throw new IllegalArgumentException("Book must have at least one author!");
		}

		return bookService.updateBookById(book);
	}

	@DgsMutation
	public boolean deleteBookById(@InputArgument String id) {
		if (id.isBlank()) {
			throw new NullPointerException("Book ID cannot be blank!");
		}

		return bookService.deleteBookById(Integer.parseInt(id));
	}
}
