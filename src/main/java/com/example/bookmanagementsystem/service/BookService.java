package com.example.bookmanagementsystem.service;

import com.example.bookmanagementsystem.dto.BookInput;
import com.example.bookmanagementsystem.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
	List<Book> getBooks(String titleFilter);

	Optional<Book> getBookById(Integer id);

	Book createBook(BookInput book);

	Book updateBookById(BookInput book);

	boolean deleteBookById(Integer id);
}
