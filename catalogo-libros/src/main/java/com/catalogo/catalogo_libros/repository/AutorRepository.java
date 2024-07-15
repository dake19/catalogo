package com.catalogo.catalogo_libros.repository;

import com.catalogo.catalogo_libros.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);
    List<Autor> findByAnoFallecimientoIsNull();
}
