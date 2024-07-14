package br.com.alura.literalura;

import br.com.alura.literalura.client.GutendexClient;
import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.service.AuthorService;
import br.com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class GutendexMain implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GutendexClient gutendexClient;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println();
            var menu = """
                    Escolha uma opção
                    1- buscar livro pelo título
                    2- listar livros registrados
                    3- listar autores registrados
                    4- listar autores vivos em um determinado ano
                    5- listar livros em um determinado idioma
                    0- sair
                    """;

            System.out.println(menu);
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    gutendexClient.searchBookByTitle();
                    break;
                case 2:
                    listAllBooks();
                    break;
                case 3:
                    listAllAuthors();
                    break;
                case 4:
                    listAliveAuthorsInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void listAllBooks() {
        List<Book> books = bookService.findAll();
        for (Book book : books) {
            System.out.println();
            System.out.println("===***=== LIVRO ===***===");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Idioma: " + book.getLanguages());

            List<Author> authors = book.getAuthors();
            if (authors != null) {
                for (Author author : authors) {
                    System.out.println("Autor: " + author.getName());
                }
            }
            System.out.println("Número de downloads: " + book.getDownloads());
            System.out.println();
        }
    }

    private void listAllAuthors() {
        List<Author> authors = authorService.findAll();

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
            return;
        }

        for (Author author : authors) {
            System.out.println("==== AUTOR ====");
            System.out.println("Nome: " + author.getName());
            System.out.println("Ano de nascimento: " + (author.getBirthYear() != null ? author.getBirthYear() : "Desconhecido"));
            System.out.println("Ano de falecimento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Desconhecido"));

            List<Book> books = bookService.findByAuthor(author);
            System.out.print("Livros: ");
            if (books.isEmpty()) {
                System.out.println("Nenhum livro registrado.");
            } else {
                for (int i = 0; i < books.size(); i++) {
                    if (i > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(books.get(i).getTitle());
                }
                System.out.println();
            }
            System.out.println();
        }
    }


    private void listAliveAuthorsInYear() {
        System.out.println("Insira o ano");
        int year = Integer.parseInt(scanner.nextLine());
        List<Author> authors = authorService.findAliveAuthorsInYear(year);
        for (Author author : authors) {
            System.out.println();
            System.out.println("==== AUTOR ====");
            System.out.println("Nome: " + author.getName());
            System.out.println("Ano de nascimento: " + author.getBirthYear());
            System.out.println("Ano de falecimento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Ainda vivo"));
            System.out.println();
        }
    }

    private void listBooksByLanguage() {
        var listBookMenu = """
                Insira o idioma para realizar a busca:
                es - espanhol
                en - inglês
                fr - francês
                pt - português
                """;
        System.out.println(listBookMenu);
        String language = scanner.nextLine();
        List<Book> books = bookService.findByLanguage(language);
        if (books.isEmpty()) {
            System.out.println("Não existem livros neste idioma no banco de dados.");
        } else {
            for (Book book : books) {
                System.out.println();
                System.out.println("==== LIVRO ====");
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
                if (book.getAuthor() != null) {
                    System.out.println("Autor: " + book.getAuthor().getName());
                }
                System.out.println("Número de downloads: " + book.getDownloads());
                System.out.println();
            }
        }
    }
}
