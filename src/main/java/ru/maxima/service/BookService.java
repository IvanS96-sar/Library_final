package ru.maxima.service;


import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.maxima.dto.BookDTO;
import ru.maxima.models.Book;
import ru.maxima.models.Person;
import ru.maxima.repositories.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book findById(Long idBook) {
        Optional<Book> byId = bookRepository.findById(idBook);
        return byId.orElse(null);

    }

    @Transactional
    public void createBook(BookDTO bookDTO) {
        Book book  = convertFromDTOToBook(bookDTO);
        book.setCreatedAt(LocalDateTime.now());
         bookRepository.save(book);


    }


    @Transactional
    public void update(Long id,BookDTO bookDTO) {
        Book book = convertFromDTOToBook(bookDTO);
        book.setIdBook(id);
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Integer idBook) {
        Book book = bookRepository.findById(Long.valueOf(idBook)).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setRemoved(true);
        book.setRemovedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    @Transactional
    public void takeBookReturn(Long idBook) {
        bookRepository.findById(idBook).orElse(null).setOwnerOfBook(null);
    }

    @Transactional
    public void giveBook(Long idBook, Person person) {

        bookRepository.findById(idBook).orElseThrow(() -> new RuntimeException("Book not found")).setOwnerOfBook(person);

    }

    public BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public Book convertFromDTOToBook(BookDTO bookDTO) {
        Book book= modelMapper.map(bookDTO, Book.class);
        book.setUpdatedAt(LocalDateTime.now());

        return book;
    }

}
