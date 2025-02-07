package com.example.bookmanagementsystem.author;

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
class AuthorServiceTest extends SharedContainerWithReuseTest {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void createAuthor() {
		String newAuthorName = "Dale Carnegie";

		String authorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(name: "%s") {
				        id
				        name
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.name");

		assertThat(authorName).isEqualTo(newAuthorName);
	}

	@Test
	void retrieveAuthorById() {
		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
			{
				retrieveAuthorById(idFilter: "1") {
					id
				}
			}
			""",
			"data.retrieveAuthorById.id");

		assertThat(authorId).isEqualTo("1");
	}

	@Test
	void retrieveAuthors() {
		String newAuthorName = "Dale Carnegie";

		newAuthorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(name: "%s") {
				        id
				        name
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.name");

		List<String> authorNames = dgsQueryExecutor.executeAndExtractJsonPath(
			"{ retrieveAuthors { name } }",
			"data.retrieveAuthors[*].name");

		assertThat(authorNames).contains(newAuthorName);
	}

	@Test
	void retrieveAuthorsWithNameFilter() {
		String newAuthorName = "Dale Carnegie";

		String authorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(name: "%s") {
				        id
				        name
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.name");

		List<String> carnegies = dgsQueryExecutor.executeAndExtractJsonPath(
			"{ retrieveAuthors(nameFilter: \"Carnegie\") { name } }",
			"data.retrieveAuthors[*].name");

		assertThat(carnegies).contains(authorName);
	}

	@Test
	void updateAuthorById() {
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

		String updateAuthorName = "D. Carnegie";

		String updatedAuthorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation updateAuthorById {
				    updateAuthorById(id : "%s", name: "%s") {
				        name
				    }
				}
				""",
				newAuthorId,
				updateAuthorName
			),
			"data.updateAuthorById.name");

		assertThat(updatedAuthorName).contains(updateAuthorName);
	}

	@Test
	void deleteAuthorById() {
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

		Boolean deleteResult = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation deleteAuthorById {
				    deleteAuthorById(id: "%s")
				}
				""",
				newAuthorId
			),
			"data.deleteAuthorById");

		assertThat(deleteResult).isTrue();
	}

	@Test
	void authorAddBook() {
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

		String newBookTitle = "The Subtle Art of Not Giving a F*ck";

		List<String> bookTitles = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation authorAddBook {
				    authorAddBook(id: "%s", book: { title: "%s" }) {
				        books {
				            title
				        }
				    }
				}
				""",
				newAuthorId,
				newBookTitle
			),
			"data.authorAddBook.books[*].title");

		assertThat(bookTitles).contains(newBookTitle);
	}

	@Test
	void authorAddBookByBookId() {
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

		String newBookTitle = "The Subtle Art of Not Giving a F*ck";

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(title: "%s", publicationYear: 2016) {
				        id
				    }
				}
				""",
				newBookTitle
			),
			"data.createBook.id");

		List<String> bookNames = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation authorAddBookByBookId {
					authorAddBookByBookId(id: "%s", bookId: "%s" ) {
						books {
							title,
						}
					}
				}
				""",
				newAuthorId,
				newBookId
			),
			"data.authorAddBookByBookId.books[*].title");

		assertThat(bookNames).contains(newBookTitle);
	}

	@Test
	void authorRemoveBookByBookId() {
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

		String newBookTitle = "The Subtle Art of Not Giving a F*ck";

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation authorAddBook {
				    authorAddBook(id: "%s", book: { title: "%s" }) {
				        books {
				            id
				        }
				    }
				}
				""",
				newAuthorId,
				newBookTitle
			),
			"data.authorAddBook.books[-1].id");

		List<String> booksId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation authorRemoveBookByBookId {
				    authorRemoveBookByBookId(id: "%s", bookId: "%s") {
				        books {
				            id
				        }
				    }
				}
				""",
				newAuthorId,
				newBookId
			),
			"data.authorRemoveBookByBookId.books[*].id");

		assertThat(booksId).doesNotContain(newBookId);
	}
}
