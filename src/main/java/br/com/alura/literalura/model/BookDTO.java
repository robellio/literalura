package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(@JsonAlias("title") String title,
                      @JsonAlias("authors") List<AuthorDTO> author,
                      @JsonAlias("languages") List<String> languages,
                      @JsonAlias("download_count") int downloads) {
}
