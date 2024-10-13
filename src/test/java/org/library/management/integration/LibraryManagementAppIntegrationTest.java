package org.library.management.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.management.models.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LibraryManagementAppIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void addBookForTest() throws Exception {
        String bookDtoJson = """
                {
                    "iSBN": "12345",
                    "title": "Title 1",
                    "author": "Author 1",
                    "publicationYear": 2020,
                    "availableCopies": 3
                }""";

        mockMvc.perform(post("/library/add/book")
                .content(bookDtoJson)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON));
    }

    @Test
    public void addBook() throws Exception {
        String bookDtoJson = """
                {
                    "iSBN": "12345",
                    "title": "Title 1",
                    "author": "Author 1",
                    "publicationYear": 2020,
                    "availableCopies": 3
                }""";
        BookDto bookDto = new BookDto("12345", "Title 1", "Author 1", 2020, 3);

        mockMvc.perform(post("/library/add/book")
                        .content(bookDtoJson)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/library/12345/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(bookDto.title())))
                .andExpect(jsonPath("$.author", is(bookDto.author())))
                .andExpect(jsonPath("$.author", is(bookDto.author())))
                .andExpect(jsonPath("$.availableCopies", is(bookDto.availableCopies() + 1)))
                .andExpect(jsonPath("$.publicationYear", is(bookDto.publicationYear())));
    }

    @Test
    public void removeBook() throws Exception {
        // Given
        //addBookForTest();
        String iSBN = "12345";

        // When
        mockMvc.perform(delete("/library/remove/"+iSBN+"/book"))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/library/1234590/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    private void borrowBookForTest() throws Exception {
        mockMvc.perform(get("/library/12345/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @AfterEach
    public void deleteBookForTest() throws Exception {
        mockMvc.perform(delete("/library/remove/12345/book"));
                //.andExpect(status().isNoContent());
    }

    @Test
    public void borrowBook() throws Exception {
        // Given
        addBookForTest(); // Available copies - 5
        String iSBN = "12345";

        // When
        mockMvc.perform(put("/library/borrow/"+iSBN+"/book"))
                .andExpect(status().isOk()); // Available copies should be - 4

        // Then
        mockMvc.perform(get("/library/12345/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.availableCopies", is(4)));

    }

    @Test
    public void returnBook() throws Exception {
        // Given
        addBookForTest(); // Available copies - 4
        borrowBookForTest(); // Available copies - 3
        String iSBN = "12345";

        // When
        mockMvc.perform(put("/library/return/"+iSBN+"/book"))
                .andExpect(status().isOk()); // Available copies should be - 4

        // Then
        mockMvc.perform(get("/library/12345/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.availableCopies", is(4)));

    }

}
