package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.model.Author;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;

public interface AuthorService {
	List<Author> retrieveAuthors(String nameFilter);

	Optional<Author> retrieveAuthorById(String idFilter);

	Author createAuthor(String name);

	Author updateAuthorById(String id, String name);

	Author authorAddBook(String id, DataFetchingEnvironment dataFetchingEnvironment);

	Author authorAddBookByBookId(String id, String bookId);

	Author authorRemoveBookByBookId(String id, String bookId);

	boolean deleteAuthorById(String id);
}
