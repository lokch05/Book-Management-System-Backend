package com.example.bookmanagementsystem.mapper;

import com.example.bookmanagementsystem.dto.AuthorDto;
import com.example.bookmanagementsystem.dto.BookDto;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import java.util.ArrayList;
import java.util.List;

public class AuthorMapper {
	public static AuthorDto authorToAuthorDto(Author author) {
		if (author == null)
			throw new IllegalArgumentException();

		List<Book> books = author.getBooks();
		List<BookDto> bookDtos = new ArrayList<>();

		for (Book book: books) {
			bookDtos.add(BookMapper.bookToBookDto(book));
		}

		return new AuthorDto(
			author.getId(),
			author.getName(),
			bookDtos
		);
	}
}
