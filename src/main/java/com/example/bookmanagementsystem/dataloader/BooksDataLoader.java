package com.example.bookmanagementsystem.dataloader;

import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.model.Book;
import com.example.bookmanagementsystem.repository.BookRepository;
import com.netflix.graphql.dgs.DgsDataLoader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;

@DgsDataLoader(name = "books")
@RequiredArgsConstructor
public class BooksDataLoader implements BatchLoader<String, Book> {

	private final BookRepository bookRepository;

	@Override
	public CompletionStage<List<Book>> load(List<String> ids) {
		List<Integer> intIds = ids.stream().map(Integer::parseInt).toList();

		return CompletableFuture.supplyAsync(() -> bookRepository.findAllById(intIds));
	}
}
