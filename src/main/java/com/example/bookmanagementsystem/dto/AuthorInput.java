package com.example.bookmanagementsystem.dto;

import java.util.List;

public record AuthorInput(
	String id,
	String name,
	List<String> booksId
) {}
