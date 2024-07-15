package com.catalogo.catalogo_libros.repository;

import com.catalogo.catalogo_libros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomasContaining(String idioma);
    @Query("SELECT l FROM Libro l JOIN FETCH l.autor a WHERE l.titulo = :titulo AND a.nombre = :autorNombre")
    Optional<Libro> findByTituloAndAutorNombre(String titulo, String autorNombre);
}
