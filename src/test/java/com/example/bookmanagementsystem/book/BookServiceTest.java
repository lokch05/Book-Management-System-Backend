package com.example.bookmanagementsystem.book;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmanagementsystem.SharedContainerWithReuseTest;
import com.example.bookmanagementsystem.config.TestApplicationConfig;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
	classes = {TestApplicationConfig.class}
)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableDgsTest
@ActiveProfiles("test")
class BookServiceTest extends SharedContainerWithReuseTest {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void createBook() {
		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(title: "The Subtle Art of Not Giving a F*ck", publicationYear: 2016) {
				        id
				        title
				        publicationYear
				    }
				}
				""",
			"data.createBook.title");

		assertThat(title).isEqualTo("The Subtle Art of Not Giving a F*ck");
	}

	@Test
	void retrieveBooks() {
		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(title: "The Subtle Art of Not Giving a F*ck", publicationYear: 2016) {
				        id
				        title
				        publicationYear
				    }
				}
				""",
			"data.createBook.title");

		List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				{
					retrieveBooks {
						title
						publicationYear
					}
				}
			""",
			"data.retrieveBooks[*].title");

		assertThat(titles).contains(title);
	}

	@Test
	void retrieveBooksWithTitleFilter() {
		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(title: "The Subtle Art of Not Giving a F*ck", publicationYear: 2016) {
				        id
				        title
				        publicationYear
				    }
				}
				""",
			"data.createBook.title");

		List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				{
					retrieveBooks(titleFilter: "Subtle") {
						title
						publicationYear
					}
				}
				""",
			"data.retrieveBooks[*].title");

		assertThat(titles).contains(title);
	}

	@Test
	void retrieveBookById() {
		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(title: "The Subtle Art of Not Giving a F*ck", publicationYear: 2016) {
				        id
				    }
				}
				""",
			"data.createBook.id");

		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
			"""
			{
				retrieveBookById(idFilter: "%s") {
					id
				}
			}
			""",
			newBookId
			),
			"data.retrieveBookById.id");

		assertThat(bookId).isEqualTo(newBookId);
	}

	@Test
	void updateBookById() {
		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(title: "The Subtle Art of Not Giving a F*ck", publicationYear: 2016) {
				        id
				        title
				        publicationYear
				    }
				}
				""",
			"data.createBook.id");

		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation updateBookById {
				    updateBookById(id: "%s", title: "The Subtle Art of Giving a F*ck", publicationYear: 1999) {
				        id
				        title
				        publicationYear
				    }
				}
				""",
				bookId
			),
			"data.updateBookById.title");

		assertThat(title).isEqualTo("The Subtle Art of Giving a F*ck");
	}

	@Test
	void deleteBookById() {
		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				mutation createBook {
				    createBook(
				        title: "How to Win Friends and Influence People",
				        publicationYear: 1936
					) {
				        id
				    }
				}
				""",
			"data.createBook.id");

		Boolean result = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation deleteBookById {
				    deleteBookById(id: "%s")
				}
				""",
				bookId),
			"data.deleteBookById");

		assertThat(result).isTrue();
	}

	@Test
	void bookAddAuthor() {
		String newBookName = "The Subtle Art of Giving a F*ck";

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(title: "%s", publicationYear: 1999) {
				        id
				    }
				}
				""",
				newBookName
			),
			"data.createBook.id"
		);

		String newAuthorName = "Dale Carnegie";

		List<String> authorNames = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation bookAddAuthor {
					bookAddAuthor(id: "%s", author: { name: "%s" }) {
						authors {
							name
						}
					}
				}
				""",
				newBookId,
				newAuthorName
			),
			"data.bookAddAuthor.authors[*].name");

		assertThat(authorNames).contains(newAuthorName);
	}

	@Test
	void bookAddAuthorByAuthorId() {
		String newBookName = "The Subtle Art of Giving a F*ck";

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(title: "%s", publicationYear: 1999) {
				        id
				    }
				}
				""",
				newBookName
			),
			"data.createBook.id"
		);

		String newAuthorName = "Dale Carnegie";

		String newAuthorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(name: "%s") {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		List<String> authorNames = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation bookAddAuthorByAuthorId {
					bookAddAuthorByAuthorId(id: "%s", authorId: "%s") {
						authors {
							name
						}
					}
				}
				""",
				newBookId,
				newAuthorId
			),
			"data.bookAddAuthorByAuthorId.authors[*].name");

		assertThat(authorNames).contains(newAuthorName);
	}

	@Test
	void bookRemoveAuthorByAuthorId() {
		String newBookName = "The Subtle Art of Giving a F*ck";

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(title: "%s", publicationYear: 1999) {
				        id
				    }
				}
				""",
				newBookName
			),
			"data.createBook.id"
		);

		String newAuthorName = "Dale Carnegie";

		String newAuthorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(name: "%s") {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		List<String> authorNames = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation bookRemoveAuthorByAuthorId {
					bookRemoveAuthorByAuthorId(id: "%s", authorId: "%s") {
						authors {
							name
						}
					}
				}
				""",
				newBookId,
				newAuthorId
			),
			"data.bookRemoveAuthorByAuthorId.authors[*].name");

		assertThat(authorNames).doesNotContain(newAuthorName);
	}
}
