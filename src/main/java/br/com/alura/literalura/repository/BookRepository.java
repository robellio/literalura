package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByLanguagesContaining(String language);
    List<Book> findByAuthor(Author author);
}
