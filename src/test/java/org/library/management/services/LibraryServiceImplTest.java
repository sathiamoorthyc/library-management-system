package org.library.management.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.management.helper.TestHelper;
import org.library.management.models.Book;
import org.library.management.models.dto.BookDto;
import org.library.management.repositories.LibraryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class LibraryServiceImplTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    @Test
    public void addBook_when_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);
        when(libraryRepository.save(any())).thenReturn(book);
        BookDto savedBookDto = libraryService.addBook(bookDto);

        // Then
        assertEquals(bookDto.iSBN(), savedBookDto.iSBN());
        assertEquals(bookDto.availableCopies() + 1, savedBookDto.availableCopies());
    }

    @Test
    public void addBook_when_no_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(Optional.empty());
        when(libraryRepository.save(any())).thenReturn(book);
        BookDto savedBookDto = libraryService.addBook(bookDto);

        // Then
        assertEquals(bookDto.iSBN(), savedBookDto.iSBN());
        assertEquals(bookDto.availableCopies() , savedBookDto.availableCopies());
    }

    @Test
    public void removeBook_when_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);
        libraryService.removeBook(bookDto.iSBN());

        // Then
        verify(libraryRepository, times(1)).deleteById(any());
    }

    @Test
    public void removeBook_when_no_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(Optional.empty());

        // Then
        NoSuchElementException noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> libraryService.removeBook(bookDto.iSBN()));

        assertEquals("No book found with the ISBN: [12345] provided.", noSuchElementException.getLocalizedMessage());

    }

    @Test
    public void findBookByIsbn_when_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);
        BookDto bookByIsbn = libraryService.findBookByIsbn(bookDto.iSBN());

        // Then
        assertNotNull(bookByIsbn);
        assertEquals(bookDto.iSBN(), bookByIsbn.iSBN());
    }

    @Test
    public void findBookByIsbn_when_no_copies_exist(){
        // Given
        String iSBN = "Invalid ISBN";

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(Optional.empty());

        // Then
        NoSuchElementException noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> libraryService.findBookByIsbn(iSBN));

        assertEquals("No book found with the ISBN: [Invalid ISBN] provided.", noSuchElementException.getLocalizedMessage());
    }

    @Test
    public void findBooksByAuthor_when_copies_exist(){
        // Given
        String author = "Author 1";
        Optional<List<Book>> books = TestHelper.buildBooks();

        // When
        when(libraryRepository.findBooksByAuthor(any())).thenReturn(books);
        List<BookDto> booksByAuthor = libraryService.findBooksByAuthor(author);

        // Then
        assertFalse(booksByAuthor.isEmpty());
        assertEquals(2, booksByAuthor.size());
        assertEquals(author, booksByAuthor.get(0).author());
        assertEquals(author, booksByAuthor.get(1).author());
    }

    @Test
    public void findBooksByAuthor_when_no_copies_exist(){
        // Given
        String author = "Invalid Author";

        // When
        when(libraryRepository.findBooksByAuthor(any())).thenReturn(Optional.empty());
        List<BookDto> booksByAuthor = libraryService.findBooksByAuthor(author);

        // Then
        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    public void borrowBook_when_copies_exist(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);
        when(libraryRepository.save(any())).thenReturn(book);
        BookDto borrowedBook = libraryService.borrowBook(bookDto.iSBN());

        // Then
        verify(libraryRepository, times(1)).save(any());
        assertNotNull(borrowedBook);
        assertEquals(bookDto.iSBN(), borrowedBook.iSBN());
        assertEquals(0, borrowedBook.availableCopies());
    }

    @Test
    public void borrowBook_when_book_does_not_exist_in_library(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(Optional.empty());

        // Then
        NoSuchElementException noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> libraryService.borrowBook(bookDto.iSBN()));

        assertEquals("No book found with the ISBN: [12345] provided in the library.", noSuchElementException.getLocalizedMessage());
    }

    @Test
    public void borrowBook_when_no_copies_available_in_library(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);
        book.setAvailableCopies(0);
        optionalBook = Optional.of(book);


        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);

        // Then
        IllegalStateException illegalStateException = assertThrowsExactly(IllegalStateException.class, () -> libraryService.borrowBook(bookDto.iSBN()));

        assertEquals("Currently there are no copies available to rent.", illegalStateException.getLocalizedMessage());
    }

    @Test
    public void returnBook(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(optionalBook);
        when(libraryRepository.save(any())).thenReturn(book);
        BookDto returnedBook = libraryService.returnBook(bookDto.iSBN());

        // Then
        verify(libraryRepository, times(1)).save(any());
        assertNotNull(returnedBook);
        assertEquals(bookDto.iSBN(), returnedBook.iSBN());
        assertEquals(2, returnedBook.availableCopies());
    }

    @Test
    public void returnBook_when_book_not_belong_to_library(){
        // Given
        BookDto bookDto = TestHelper.buildBookDto();
        Optional<Book> optionalBook = TestHelper.buildBook();
        Book book = optionalBook.orElse(null);

        // When
        when(libraryRepository.findBookByiSBN(any())).thenReturn(Optional.empty());
        //when(libraryRepository.save(any())).thenReturn(book);

        // Then
        NoSuchElementException noSuchElementException = assertThrowsExactly(NoSuchElementException.class, () -> libraryService.returnBook(bookDto.iSBN()));

        assertEquals("The book with ISBN: 12345 does not belong to this library.", noSuchElementException.getLocalizedMessage());
    }
}