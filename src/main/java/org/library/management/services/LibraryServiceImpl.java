package org.library.management.services;

import org.library.management.models.Book;
import org.library.management.models.dto.BookDto;
import org.library.management.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "books")
public class LibraryServiceImpl implements LibraryService{

    @Autowired
    private LibraryRepository libraryRepository;

    public BookDto addBook(BookDto bookDto){
        Optional<Book> bookByIsbn = libraryRepository.findBookByiSBN(bookDto.iSBN());
        if(bookByIsbn.isPresent()) {
            Book existingBook = bookByIsbn.get();
            existingBook.setAvailableCopies(existingBook.getAvailableCopies() + 1);
            Book savedBook = libraryRepository.save(existingBook);
            return convertBookToBookDto(savedBook);
        }
        Book newBook = convertBookDtoToBook(bookDto);
        return convertBookToBookDto(libraryRepository.save(newBook));
    }

    private BookDto convertBookToBookDto(Book book){
        return new BookDto(book.getISBN(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getAvailableCopies());
    }

    private Book convertBookDtoToBook(BookDto bookDto){
        return Book.builder()
                .title(bookDto.title())
                .author(bookDto.author())
                .iSBN(bookDto.iSBN())
                .publicationYear(bookDto.publicationYear())
                .availableCopies(bookDto.availableCopies())
                .build();
    }

    public void removeBook(String iSBN){
        Optional<Book> bookByIsbn = libraryRepository.findBookByiSBN(iSBN);
        if(bookByIsbn.isPresent()) {
            libraryRepository.deleteById(bookByIsbn.get().getId());
        }else{
            throw new NoSuchElementException("No book found with the ISBN: [" + iSBN + "] provided.");
        }
    }

    @Cacheable(cacheNames = {"books"}, key = "#iSBN")
    public BookDto findBookByIsbn(String iSBN){
        Optional<Book> bookByiSBN = libraryRepository.findBookByiSBN(iSBN);
        return convertBookToBookDto(bookByiSBN.orElseThrow(() -> new NoSuchElementException("No book found with the ISBN: [" + iSBN + "] provided.")));
    }

    @Cacheable(cacheNames = {"books"}, key = "#author")
    public List<BookDto> findBooksByAuthor(String author){
        Optional<List<Book>> booksByAuthor = libraryRepository.findBooksByAuthor(author);
        List<Book> books = booksByAuthor.orElse(Collections.emptyList());
        return books.stream().map(this::convertBookToBookDto).toList();
    }

    public BookDto borrowBook(String iSBN){
        Optional<Book> bookByIsbn = libraryRepository.findBookByiSBN(iSBN);
        if(bookByIsbn.isPresent()){
            Book book = bookByIsbn.get();
            if(book.getAvailableCopies() > 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                return convertBookToBookDto(libraryRepository.save(book));
            }else{
                throw new IllegalStateException("Currently there are no copies available to rent.");
            }
        }
        throw new NoSuchElementException("No book found with the ISBN: [" + iSBN + "] provided in the library.");
    }

    public BookDto returnBook(String iSBN){
        Optional<Book> bookByIsbn = libraryRepository.findBookByiSBN(iSBN);
        if(bookByIsbn.isPresent()){
            Book book = bookByIsbn.get();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            return convertBookToBookDto(libraryRepository.save(book));
        }
        throw new NoSuchElementException("The book with ISBN: " + iSBN + " does not belong to this library.");
    }
}
