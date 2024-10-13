package org.library.management.helper;

import org.library.management.models.Book;
import org.library.management.models.dto.BookDto;

import java.util.List;
import java.util.Optional;

public class TestHelper {

    public static BookDto buildBookDto(){
        return new BookDto("12345", "Title", "Author", 2020, 1);
    }

    public static List<BookDto> buildBuildDtos(){
        return List.of(
                new BookDto("12345", "Title 1", "Author 1", 2021, 1),
                new BookDto("45678", "Title 2", "Author 1", 2022, 1)
        );
    }

    public static Optional<Book> buildBook(){
        return Optional.of(Book.builder().id(1).iSBN("12345").title("Title").author("Author").publicationYear(2020).availableCopies(1).build());
    }

    public static Optional<List<Book>> buildBooks(){
        return Optional.of(
                List.of(
                        Book.builder().id(1).iSBN("12345").title("Title 1").author("Author 1").publicationYear(2020).availableCopies(1).build(),
                        Book.builder().id(2).iSBN("098765").title("Title 2").author("Author 1").publicationYear(2021).availableCopies(1).build()
                )
        );
    }
}
