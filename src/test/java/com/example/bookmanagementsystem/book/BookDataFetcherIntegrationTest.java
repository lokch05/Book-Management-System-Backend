package com.example.bookmanagementsystem.book;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmanagementsystem.SharedContainerWithReuseTest;
import com.example.bookmanagementsystem.config.TestApplicationConfig;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import java.util.List;
import java.util.Map;
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
class BookDataFetcherIntegrationTest extends SharedContainerWithReuseTest {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void createBook() {
		String newAuthorName = "Dale Carnegie";

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "The Subtle Art of Not Giving a F*ck",
				            publicationYear: 2016
				            authorsId: ["%s"]
				    }) {
				        title
				    }
				}
				""",
				authorId
				),
			"data.createBook.title");

		assertThat(title).isEqualTo("The Subtle Art of Not Giving a F*ck");
	}

	@Test
	void getBooks() {
		String newAuthorName = "Dale Carnegie";

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "The Subtle Art of Not Giving a F*ck",
				            publicationYear: 2016
				            authorsId: ["%s"]
				    }) {
				        title
				    }
				}
				""",
				authorId
			),
			"data.createBook.title");

		List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				{
					getBooks {
						title
					}
				}
			""",
			"data.getBooks[*].title");

		assertThat(titles).contains(title);
	}

	@Test
	void getBooksWithTitleFilter() {
		String newAuthorName = "Dale Carnegie";

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		String title = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "The Subtle Art of Not Giving a F*ck",
				            publicationYear: 2016
				            authorsId: ["%s"]
				    }) {
				        title
				    }
				}
				""",
				authorId
			),
			"data.createBook.title");

		List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
				{
					getBooks(titleFilter: "Subtle") {
						title
					}
				}
				""",
			"data.getBooks[*].title");

		assertThat(titles).contains(title);
	}

	@Test
	void getBookById() {
		String newAuthorName = "Dale Carnegie";

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		String newBookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "The Subtle Art of Not Giving a F*ck",
				            publicationYear: 2016
				            authorsId: ["%s"]
				    }) {
				        id
				    }
				}
				""",
				authorId
			),
			"data.createBook.id");

		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
			"""
			{
				getBookById(id: "%s") {
					id
				}
			}
			""",
			newBookId
			),
			"data.getBookById.id");

		assertThat(bookId).isEqualTo(newBookId);
	}

	@Test
	void updateBookById() {
		String daleId = dgsQueryExecutor.executeAndExtractJsonPath(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "Dale Carnegie"
				    }) {
				        id
				    }
				}
				""",
			"data.createAuthor.id");

		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "The Subtle Art of Not Giving a F*ck",
				            publicationYear: 2016
				            authorsId: ["%s"]
				        }) {
				            id
					}
				}
				""",
				daleId
			),
			"data.createBook.id");

		String willyId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
			mutation createAuthor {
				createAuthor(author: {
					name: "Willy Wonka"
				}) {
					id
				}
			}
			""",
			"data.createAuthor.id");

		Map<String, ?> data = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation updateBookById {
				    updateBookById(
				        book: {
				            id: "%s",
				            title: "The Subtle Art of Giving a F*ck",
				            publicationYear: 1999
				            authorsId: ["%s"]
						}) {
				            title
				            publicationYear
				            authors {
				                name
				            }
					}
				}
				""",
				bookId,
				willyId
			),
			"data.updateBookById");

		assertThat(data.get("title")).isEqualTo("The Subtle Art of Giving a F*ck");
		assertThat(data.get("publicationYear")).isEqualTo(1999);
		assertThat(data.get("authors")).isInstanceOf(List.class);

		List<Map<String, ?>> authorsName = (List<Map<String,?>>) data.get("authors");

		assertThat(authorsName.get(0).get("name")).isEqualTo("Willy Wonka");
	}

	@Test
	void deleteBookById() {
		String newAuthorName = "Dale Carnegie";

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        id
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.id");

		String bookId = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createBook {
				    createBook(
				        book: {
				            title: "How to Win Friends and Influence People",
				            publicationYear: 1936,
				            authorsId: ["%s"]
				        }
					) {
				        id
				    }
				}
				""",
				authorId),
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
}
