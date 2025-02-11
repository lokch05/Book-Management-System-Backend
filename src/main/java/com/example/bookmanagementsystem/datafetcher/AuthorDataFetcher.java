package com.example.bookmanagementsystem.datafetcher;

import com.example.bookmanagementsystem.dataloader.AuthorsDataLoader;
import com.example.bookmanagementsystem.dto.AuthorInput;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.service.AuthorService;
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
public class AuthorDataFetcher {

	private final AuthorService authorService;

	@DgsData(parentType = "Book")
	public CompletableFuture<Author> author(DgsDataFetchingEnvironment dfe) {
		DataLoader<String, Author> dataLoader = dfe.getDataLoader(AuthorsDataLoader.class);
		String id = dfe.getArgument("id");

		return dataLoader.load(id);
	}

	@DgsQuery
	public List<Author> getAuthors(@InputArgument String nameFilter) {
		return authorService.getAuthors(nameFilter);
	}

	@DgsQuery
	public Optional<Author> getAuthorById(@InputArgument String id) {
		if (id.isBlank()) {
			throw new NullPointerException("Author ID cannot be blank!");
		}

		return authorService.getAuthorById(Integer.parseInt(id));
	}

	@DgsMutation
	public Author createAuthor(@InputArgument AuthorInput author) {
		if (author.name().isBlank()) {
			throw new IllegalArgumentException("Author name cannot be blank!");
		}

		return authorService.createAuthor(author);
	}

	@DgsMutation
	public Author updateAuthorById(@InputArgument AuthorInput author) {
		if (author.id().isBlank()) {
			throw new IllegalArgumentException("Author ID cannot be blank!");
		}

		if (author.name().isBlank()) {
			throw new IllegalArgumentException("Author name cannot be blank!");
		}

		return authorService.updateAuthorById(author);
	}

	@DgsMutation
	public boolean deleteAuthorById(@InputArgument String id) {
		if (id.isBlank()) {
			throw new NullPointerException("Author ID cannot be blank!");
		}

		return authorService.deleteAuthorById(Integer.parseInt(id));
	}
}
