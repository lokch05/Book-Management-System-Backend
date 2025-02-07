package com.example.bookmanagementsystem.repository;

import com.example.bookmanagementsystem.model.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
	List<Author> findByName(String name);

	List<Author> findByNameContainingIgnoreCase(String name);
}
