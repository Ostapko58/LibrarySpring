package com.onyshkiv.libraryspring.service;

import com.onyshkiv.libraryspring.entity.Book;
import com.onyshkiv.libraryspring.exception.author.AuthorNotFoundException;
import com.onyshkiv.libraryspring.exception.book.BookNotCreatedException;
import com.onyshkiv.libraryspring.exception.book.BookNotFoundException;
import com.onyshkiv.libraryspring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    //todo solve n+1 problem everywhere
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty()) throw new BookNotFoundException("Not book with isbn " + isbn);
        return optionalBook.get();
    }


    @Transactional
    public Book saveBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findById(book.getIsbn());
        if (optionalBook.isPresent())
            throw new BookNotCreatedException("Book with isbn " + book.getIsbn() + " already exist");
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(String isbn, Book book) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty())
            throw new BookNotFoundException("Not Book found with isbn " + isbn);
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Transactional
    public Book deleteBookByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty())
            throw new BookNotFoundException("Not Book found with isbn " + isbn);
        bookRepository.deleteById(isbn);
        return optionalBook.get();
    }


}
