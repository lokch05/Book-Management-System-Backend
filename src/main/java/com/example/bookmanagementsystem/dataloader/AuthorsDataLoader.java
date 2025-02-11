package com.example.bookmanagementsystem.dataloader;

import com.example.bookmanagementsystem.model.Author;
import com.example.bookmanagementsystem.repository.AuthorRepository;
import com.netflix.graphql.dgs.DgsDataLoader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;

@DgsDataLoader(name = "authors")
@RequiredArgsConstructor
public class AuthorsDataLoader implements BatchLoader<String, Author> {

	private final AuthorRepository authorRepository;

	@Override
	public CompletionStage<List<Author>> load(List<String> ids) {
		List<Integer> intIds = ids.stream().map(Integer::parseInt).toList();

		return CompletableFuture.supplyAsync(() -> authorRepository.findAllById(intIds));
	}
}
