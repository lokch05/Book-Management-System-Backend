package com.example.bookmanagementsystem.mapper;

import com.example.bookmanagementsystem.dto.AuthorDto;
import com.example.bookmanagementsystem.dto.BookDto;
import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookMapper {
	public static BookDto bookToBookDto(Book book) {
		if (book == null)
			throw new IllegalArgumentException();

		List<Author> authors = book.getAuthors();
		List<AuthorDto> authorDtos = new ArrayList<>();

		for (Author author: authors) {
			authorDtos.add(AuthorMapper.authorToAuthorDto(author));
		}

		return new BookDto(
			book.getId(),
			book.getTitle(),
			book.getPublicationYear(),
			authorDtos
		);
	}
}
