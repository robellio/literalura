package br.com.alura.literalura.controller;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/alive-in-year/{year}")
    public ResponseEntity<List<Author>> getAliveAuthorsInYear(@PathVariable int year) {
        return ResponseEntity.ok(authorService.findAliveAuthorsInYear(year));
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.save(author));
    }
}
