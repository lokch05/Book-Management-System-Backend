package com.example.bookmanagementsystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence_generator")
	@SequenceGenerator(
		name = "book_sequence_generator",
		sequenceName = "book_sequence",
		allocationSize = 1)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private int id;

	@Column(nullable = false)
	private String title;

	@Column(name = "publication_year")
	private int publicationYear;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
		name = "book_author",
		joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> authors = List.of();

	public Book(String title, List<Author> authors) {
		this.title = title;
		this.authors = List.copyOf(authors);
	}

	public Book(String title, int publicationYear, List<Author> authors) {
		this.title = title;
		this.publicationYear = publicationYear;
		this.authors = List.copyOf(authors);
	}
}
