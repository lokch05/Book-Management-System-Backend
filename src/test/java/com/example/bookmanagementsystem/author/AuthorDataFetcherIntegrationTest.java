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
class AuthorDataFetcherIntegrationTest extends SharedContainerWithReuseTest {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void createAuthor() {
		String newAuthorName = "Dale Carnegie";

		String authorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
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
	void getAuthorById() {
		String id = dgsQueryExecutor.executeAndExtractJsonPath(
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

		String authorId = dgsQueryExecutor.executeAndExtractJsonPath(
			"""
			{
				getAuthorById(id: "1") {
					id
				}
			}
			""",
			"data.getAuthorById.id");

		assertThat(authorId).isEqualTo(id);
	}

	@Test
	void getAuthors() {
		String newAuthorName = "Dale Carnegie";

		newAuthorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        name
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.name");

		List<String> authorNames = dgsQueryExecutor.executeAndExtractJsonPath(
			"{ getAuthors { name } }",
			"data.getAuthors[*].name");

		assertThat(authorNames).contains(newAuthorName);
	}

	@Test
	void getAuthorsWithNameFilter() {
		String newAuthorName = "Dale Carnegie";

		String authorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation createAuthor {
				    createAuthor(author: {
				        name: "%s"
				    }) {
				        name
				    }
				}
				""",
				newAuthorName
			),
			"data.createAuthor.name");

		List<String> carnegies = dgsQueryExecutor.executeAndExtractJsonPath(
			"{ getAuthors(nameFilter: \"Carnegie\") { name } }",
			"data.getAuthors[*].name");

		assertThat(carnegies).contains(authorName);
	}

	@Test
	void updateAuthorById() {
		String newAuthorName = "Dale Carnegie";

		String newAuthorId = dgsQueryExecutor.executeAndExtractJsonPath(
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

		String updateAuthorName = "D. Carnegie";

		String updatedAuthorName = dgsQueryExecutor.executeAndExtractJsonPath(
			String.format(
				"""
				mutation updateAuthorById {
				    updateAuthorById(author: {
				        id: "%s"
				        name: "%s"
				    }) {
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
}
