package com.catalogo.catalogo_libros;

import com.catalogo.catalogo_libros.model.Libro;
import com.catalogo.catalogo_libros.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class Menu {

    @Autowired
    private GutendexService gutendexService;

    public boolean mostrarMenu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menú:");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar todos los libros");
        System.out.println("3. Listar autores");
        System.out.println("4. Listar autores vivos en determinado año");
        System.out.println("5. Exhibir cantidad de libros en un determinado idioma");
        System.out.println("6. Salir");
        System.out.print("Elige una opción: ");

        try {
            String opcion = reader.readLine();

            switch (opcion) {
                case "1":
                    System.out.print("Introduce el título del libro: ");
                    String titulo = reader.readLine();
                    List<Libro> libros = gutendexService.buscarLibroPorTitulo(titulo);
                    mostrarLibros(libros);
                    break;
                case "2":
                    // Lógica para listar todos los libros
                    break;
                case "3":
                    // Lógica para listar autores
                    break;
                case "4":
                    // Lógica para listar autores vivos en determinado año
                    break;
                case "5":
                    // Lógica para exhibir cantidad de libros en un determinado idioma
                    break;
                case "6":
                    System.out.println("Saliendo...");
                    return false;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }
    private void mostrarLibros(List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros.");
        } else {
            for (Libro libro : libros) {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor().getNombre());
                System.out.println("Idiomas: " + libro.getIdiomas());
                System.out.println("Número de Descargas: " + libro.getNumeroDescargas());
                System.out.println("---------------------------------");
            }
        }
    }
}
