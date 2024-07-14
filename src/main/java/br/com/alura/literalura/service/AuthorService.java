package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public List<Author> findAliveAuthorsInYear(int year) {
        return authorRepository.findByDeathYearIsNullOrDeathYearGreaterThanEqual(year);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }
}

