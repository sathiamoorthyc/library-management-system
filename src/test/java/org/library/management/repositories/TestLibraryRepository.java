package org.library.management.repositories;

import org.library.management.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestLibraryRepository extends JpaRepository<Book, Integer> {
}
