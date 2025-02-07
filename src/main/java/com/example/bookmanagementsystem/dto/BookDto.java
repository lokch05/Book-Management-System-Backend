package com.example.bookmanagementsystem.dto;

import java.util.List;

public record BookDto(int id, String title, int publicationYear, List<AuthorDto> authors) {

}
