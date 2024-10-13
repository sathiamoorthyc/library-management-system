package org.library.management.controllers;

import lombok.RequiredArgsConstructor;
import org.library.management.models.dto.BookDto;
import org.library.management.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping(value = "/{iSBN}/books-by-isbn")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbn(@PathVariable String iSBN) {
        return libraryService.findBookByIsbn(iSBN);
    }

    @GetMapping(value = "/{author}/books-by-author")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@PathVariable String author) {
        List<BookDto> booksByAuthor = libraryService.findBooksByAuthor(author);
        return new ResponseEntity<>(booksByAuthor, booksByAuthor.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(value = "/add/book", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> addBook(@RequestBody BookDto bookDto) {
        libraryService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/remove/{iSBN}/book")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> removeBook(@PathVariable String iSBN) {
        libraryService.removeBook(iSBN);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/borrow/{iSBN}/book")
    @ResponseStatus(HttpStatus.OK)
    public BookDto borrowBook(@PathVariable String iSBN) {
        return libraryService.borrowBook(iSBN);
    }

    @PutMapping(value = "/return/{iSBN}/book")
    @ResponseStatus(HttpStatus.OK)
    public BookDto returnBook(@PathVariable String iSBN) { return libraryService.returnBook(iSBN);
    }

}
