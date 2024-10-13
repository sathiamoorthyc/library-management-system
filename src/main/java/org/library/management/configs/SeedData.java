package org.library.management.configs;

import org.library.management.models.Book;
import org.library.management.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    LibraryRepository libraryRepository;

    @Override
    public void run(String... args) throws Exception {

        Book book1 = Book.builder().iSBN("12345").title("Machine Learning").author("Author 1").publicationYear(2020).availableCopies(2).build();
        Book book2 = Book.builder().iSBN("09876").title("Artificial Intelligence").author("Author 2").publicationYear(2021).availableCopies(5).build();

        libraryRepository.save(book1);
        libraryRepository.save(book2);
    }
}
