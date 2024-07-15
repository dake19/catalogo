package com.catalogo.catalogo_libros.service;

import com.catalogo.catalogo_libros.exception.LibroYaRegistradoException;
import com.catalogo.catalogo_libros.model.Autor;
import com.catalogo.catalogo_libros.model.Libro;
import com.catalogo.catalogo_libros.repository.AutorRepository;
import com.catalogo.catalogo_libros.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GutendexService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    // RestTemplate para hacer solicitudes HTTP
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Método para buscar libros para títulos en la API de Gutendex
    public List<Libro> buscarLibroPorTitulo(String titulo) throws IOException {
        String url = "https://gutendex.com/books?search=" + titulo;
        String response = restTemplate.getForObject(url, String.class);
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode results = rootNode.path("results");

        List<Libro> librosEncontrados = new ArrayList<>();

        // Iterar sobre los resultados
        for (JsonNode node : results) {
            String bookTitle = node.path("title").asText();
            JsonNode authorsNode = node.path("authors");
            String languages = node.path("languages").get(0).asText();
            int downloadCount = node.path("download_count").asInt();

            // procesar cada autor del libro
            for (JsonNode authorNode : authorsNode) {
                String authorName = authorNode.path("name").asText();
                Integer birthYear = authorNode.has("birth_year") ? authorNode.path("birth_year").asInt() : null;
                Integer deathYear = authorNode.has("death_year") ? authorNode.path("death_year").asInt() : null;
                Optional<Autor> optionalAutor = autorRepository.findByNombre(authorName);
                Autor autor;
                if (optionalAutor.isPresent()) {
                    autor = optionalAutor.get();
                } else {
                    autor = new Autor();
                    autor.setNombre(authorName);
                    autor.setAnoNacimiento(birthYear);
                    autor.setAnoFallecimiento(deathYear);
                    autorRepository.save(autor);
                }

                //crear y guardar el libro en la base de datos
                Optional<Libro> optionalLibro = libroRepository.findByTituloAndAutorNombre(bookTitle, authorName);
                if (optionalLibro.isPresent()) {
                    throw new LibroYaRegistradoException("No se puede registrar un libro mas de una vez");
                }
                if (optionalLibro.isEmpty()) {
                    Libro libro = new Libro();
                    libro.setTitulo(bookTitle);
                    libro.setAutor(autor);
                    libro.setIdiomas(languages);
                    libro.setNumeroDescargas(downloadCount);

                    libroRepository.save(libro);
                    librosEncontrados.add(libro);
                }

            }
        }
        return librosEncontrados;
    }




}
