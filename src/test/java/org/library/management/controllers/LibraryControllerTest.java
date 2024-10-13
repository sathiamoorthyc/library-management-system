package org.library.management.controllers;

import org.junit.jupiter.api.Test;
import org.library.management.helper.TestHelper;
import org.library.management.models.dto.BookDto;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LibraryControllerTest extends AbstractControllerTest{

    @Test
    public void getBookByIsbn() throws Exception {
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        String iSBN = bookDto.iSBN();

        // When
            when(libraryService.findBookByIsbn(iSBN)).thenReturn(bookDto);

        // Then
        mockMvc.perform(get("/library/"+ iSBN + "/books-by-isbn").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.author", is("Author")))
                .andExpect(jsonPath("$.publicationYear", is(2020)));
    }

    @Test
    public void getBooksByAuthor_Found() throws Exception {
        // Given
        List<BookDto> bookDtos = TestHelper.buildBuildDtos();
        String author = bookDtos.get(0).author();

        // When
        when(libraryService.findBooksByAuthor(author)).thenReturn(bookDtos);

        // Then
        mockMvc.perform(get("/library/"+ author+"/books-by-author").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Title 1")))
                .andExpect(jsonPath("$[0].author", is("Author 1")))
                .andExpect(jsonPath("$[0].publicationYear", is(2021)))
                .andExpect(jsonPath("$[1].title", is("Title 2")))
                .andExpect(jsonPath("$[1].author", is("Author 1")))
                .andExpect(jsonPath("$[1].publicationYear", is(2022)));
    }

    @Test
    public void getBooksByAuthor_NotFound() throws Exception {
        // Given
        String author = "Unknown Author";

        // When
        when(libraryService.findBooksByAuthor(author)).thenReturn(Collections.emptyList());

        // Then
        mockMvc.perform(get("/library/"+ author+"/books-by-author").accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void addBook() throws Exception {
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        String bookDtoJson = "{\n" +
                "    \"iSBN\": \"ABC987\",\n" +
                "    \"title\": \"Machine Learning Part 2\",\n" +
                "    \"author\": \"Author 1\",\n" +
                "    \"publicationYear\": 2022,\n" +
                "    \"availableCopies\": 4\n" +
                "}";

        // When
        when(libraryService.addBook(any())).thenReturn(bookDto);

        // Then
        mockMvc.perform(post("/library/add/book")
                        .content(bookDtoJson)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(libraryService, times(1)).addBook(any());
    }

    @Test
    public void removeBook() throws Exception {
        // Given
        String iSBN = "12345";

        // When
        mockMvc.perform(delete("/library/remove/"+iSBN+"/book"))
                .andExpect(status().isNoContent());

        // Then
        verify(libraryService, times(1)).removeBook(iSBN);
    }

    @Test
    public void borrowBook() throws Exception {
        // Given
        String iSBN = "12345";

        // When
        mockMvc.perform(put("/library/borrow/"+iSBN+"/book"))
                .andExpect(status().isOk());

        // Then
        verify(libraryService, times(1)).borrowBook(iSBN);
    }

    @Test
    public void returnBook() throws Exception {
        // Given
        String iSBN = "12345";

        // When
        mockMvc.perform(put("/library/return/"+iSBN+"/book"))
                .andExpect(status().isOk());

        // Then
        verify(libraryService, times(1)).returnBook(iSBN);
    }
}