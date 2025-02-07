package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.model.Book;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;

public interface BookService {
	List<Book> retrieveBooks(String titleFilter);

	Optional<Book> retrieveBookById(String idFilter);

	Book createBook(String name, Integer publicationYear);

	Book updateBookById(String id, String name, Integer publicationYear);

	Book bookAddAuthor(String id, DataFetchingEnvironment dataFetchingEnvironment);

	Book bookAddAuthorByAuthorId(String id, String authorId);

	Book bookRemoveAuthorByAuthorId(String id, String authorId);

	boolean deleteBookById(String id);
}
