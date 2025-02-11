package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.dto.AuthorInput;
import com.example.bookmanagementsystem.model.Author;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AuthorService {
	List<Author> getAuthors(String nameFilter);

	List<Author> getAuthorsByIds(List<Integer> ids);

	Optional<Author> getAuthorById(Integer id);

	Author createAuthor(AuthorInput author);

	Author updateAuthorById(AuthorInput authorInput);

	boolean deleteAuthorById(Integer id);
}
