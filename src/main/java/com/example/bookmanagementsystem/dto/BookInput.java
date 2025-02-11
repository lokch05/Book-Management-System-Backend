package com.example.bookmanagementsystem.dto;

import java.util.List;

public record BookInput(
	String id,
	String title,
	Integer publicationYear,
	List<String> authorsId
) {}
