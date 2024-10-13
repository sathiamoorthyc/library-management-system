package org.library.management.services;

import org.library.management.models.dto.BookDto;

import java.util.List;

public interface LibraryService {
    BookDto addBook(BookDto bookDto);

    void removeBook(String iSBN);

    BookDto findBookByIsbn(String iSBN);

    List<BookDto> findBooksByAuthor(String author);

    BookDto borrowBook(String iSBN);

    BookDto returnBook(String iSBN);
}
