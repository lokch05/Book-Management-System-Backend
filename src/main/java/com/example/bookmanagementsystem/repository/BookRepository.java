package com.example.bookmanagementsystem.repository;

import com.example.bookmanagementsystem.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	List<Book> findByTitle(String title);

	List<Book> findByTitleContainingIgnoreCase(String title);
}
