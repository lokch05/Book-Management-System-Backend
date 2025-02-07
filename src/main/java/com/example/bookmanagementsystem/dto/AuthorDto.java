package com.example.bookmanagementsystem.dto;

import java.util.List;

public record AuthorDto(int id, String name, List<BookDto> books) {

}

