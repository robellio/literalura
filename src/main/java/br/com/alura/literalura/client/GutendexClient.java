package br.com.alura.literalura.client;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.AuthorDTO;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.model.BookDTO;
import br.com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class GutendexClient {

    private static final String BASE_URL = "https://gutendex.com/books/";
    private final Scanner scanner = new Scanner(System.in);


    @Autowired
    private BookService bookService;

    public void searchBookByTitle() {
        System.out.println("Insira o nome do livro que você deseja procurar");
        String title = scanner.nextLine();
        List<Book> books = bookService.findByTitle(title);

        if (books.isEmpty()) {
            List<BookDTO> apiBooks = bookService.fetchBooksFromApi(title);
            for (BookDTO bookDTO : apiBooks) {
                Book book = convertDtoToEntity(bookDTO);
                bookService.save(book);
                books.add(book);
            }
        }

        for (Book book : books) {
            System.out.println("===***=== LIVRO ===***===");
            System.out.println("Título: " + book.getTitle());
            List<String> languages = book.getLanguages();
            if (languages != null && !languages.isEmpty()) {
                System.out.print("Idioma: ");
                for (int i = 0; i < languages.size(); i++) {
                    if (i > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(languages.get(i));
                }
                System.out.println();
            }
            Author author = book.getAuthor();
            if (author != null) {
                System.out.println("Autor: " + author.getName());
            }
            System.out.println("Número de downloads: " + book.getDownloads());
            System.out.println();
        }
    }
    private Book convertDtoToEntity(BookDTO bookDTO) {
        List<Author> authors = new ArrayList<>();
        for (AuthorDTO authorDTO : bookDTO.author()) {
            Author author = new Author(authorDTO.name(), authorDTO.birthYear(), authorDTO.deathYear());
            authors.add(author);
        }
        List<String> languages = new ArrayList<>(bookDTO.languages());

        Author mainAuthor = authors.isEmpty() ? null : authors.get(0);
        return new Book(bookDTO.title(), languages, bookDTO.downloads(), mainAuthor);
    }

}
