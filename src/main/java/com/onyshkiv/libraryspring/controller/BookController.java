package com.onyshkiv.libraryspring.controller;

import com.onyshkiv.libraryspring.DTO.BookDTO;
import com.onyshkiv.libraryspring.entity.Book;
import com.onyshkiv.libraryspring.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }


    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }


}
