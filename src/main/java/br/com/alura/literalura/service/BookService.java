package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.model.BookDTO;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.repository.AuthorRepository;
import br.com.alura.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public List<Book> findByLanguage(String language) {
        return bookRepository.findByLanguagesContaining(language);
    }

    public List<Book> findByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    public List<BookDTO> fetchBooksFromApi(String title) {
        String apiUrl = "https://gutendex.com/books?search=" + title;
        ResponseEntity<GutendexResponse> response = restTemplate.getForEntity(apiUrl, GutendexResponse.class);

        if (response.getBody() != null) {
            return response.getBody().getBooks();
        }
        return new ArrayList<>();
    }

    public Book save(Book book) {
        Author author = book.getAuthor();
        if (author != null) {
            Author existingAuthor = authorRepository.findByName(author.getName());
            if (existingAuthor == null) {
                author = authorRepository.save(author);
            } else {
                author = existingAuthor;
            }
            book.setAuthor(author);
        }
        return bookRepository.save(book);
    }

    public Book convertDtoToEntity(Book book) {
        Author author = new Author(book.getAuthor().getName(), book.getAuthor().getBirthYear(), book.getAuthor().getDeathYear());
        List<String> languages = new ArrayList<>(book.getLanguages());
        return new Book(book.getTitle(), languages, book.getDownloads(), author);
    }
}
