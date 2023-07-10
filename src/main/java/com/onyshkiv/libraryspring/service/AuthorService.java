package com.onyshkiv.libraryspring.service;

import com.onyshkiv.libraryspring.entity.Author;
import com.onyshkiv.libraryspring.exception.AuthorNotFoundException;
import com.onyshkiv.libraryspring.exception.AuthorNotSavedException;
import com.onyshkiv.libraryspring.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }


    public Author getAuthorById(int id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isEmpty())
            throw new AuthorNotFoundException("Not author found with id " + id);//todo може то ліпше вертати оптіонал і вже в контроллері це робити
        return optionalAuthor.get();

    }

    @Transactional
    public Author saveAuthor(Author author) {
        if (author.getAuthorId() != 0)
            throw new AuthorNotSavedException("Author with id " + author.getAuthorId() + " already exist");
        return authorRepository.save(author);
    }

    @Transactional
    public Author updateAuthor(Integer id, Author author) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isEmpty())
            throw new AuthorNotFoundException("Not author found with id " + id);
        author.setAuthorId(id);
        return authorRepository.save(author);
    }

    @Transactional
    public Author deleteAuthorById(int id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isEmpty())
            throw new AuthorNotFoundException("Not author found with id " + id);
        authorRepository.deleteById(id);
        return optionalAuthor.get();
    }
}
