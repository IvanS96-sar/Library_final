package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.exceptions.PersonErrorResponse;
import ru.maxima.exceptions.PersonNotFoundException;
import ru.maxima.models.Book;
import ru.maxima.models.Person;
import ru.maxima.service.BookService;
import ru.maxima.service.PersonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookingController {
    private final PersonService personService;
    private final BookService bookService;


    @Autowired
    public BookingController(@Qualifier("personService") PersonService personService, @Qualifier("bookService")BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }
    @GetMapping()
    public List<BookDTO> showAllBook() {
        List<Book> allBook = bookService.getAllBook();
        List<BookDTO> bookDTOList = new ArrayList<>();
        for (Book b :allBook) {
          bookDTOList.add(bookService.convertToBookDTO(b));
        }
        return bookDTOList;
    }
    @GetMapping("/{id}")
    public BookDTO showBookById(@PathVariable("id") Long id){
        Book byId = bookService.findById(id);
        return bookService.convertToBookDTO(byId);
    }
    @ExceptionHandler({ PersonNotFoundException.class})
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException ex) {
        PersonErrorResponse response = new PersonErrorResponse(ex.getMessage(), new Date());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createNewBook(@RequestBody BookDTO bookDTO){
        bookService.createBook(bookDTO);
    }



    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void editBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){
       bookService.update(id,bookDTO);

    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(@PathVariable("id") Integer id) {
         bookService.deleteBook(id);
    }

    @PostMapping("/{id}/give")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void giveBook( @PathVariable Person person, @PathVariable Long id) {
        bookService.giveBook(id,person);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void returnBook(@PathVariable("id") Long id) {
         bookService.takeBookReturn(id);
    }

}
