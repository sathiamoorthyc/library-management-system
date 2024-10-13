package org.library.management.repositories;

import org.library.management.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findBookByiSBN(String iSBN);

    Optional<List<Book>> findBooksByAuthor(String author);

}
