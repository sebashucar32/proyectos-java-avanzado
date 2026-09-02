package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Libro;
import org.example.util.ComunRepository;
import org.example.util.JsonFileManager;

import java.io.IOException;
import java.util.List;

public class JsonLibroRepository extends ComunRepository<Libro> implements LibroRepository {
    private static final String FILE_PATH = "data/Libro.json";

    public JsonLibroRepository(JsonFileManager jsonFileManager) {
        super(jsonFileManager,"data/Libro.json", new TypeReference<>() {});
    }

    @Override
    public void editar(int isbn, Libro libro) {
        try {
            List<Libro> libros = listar();

            libros.stream()
                .filter(l -> l.getIsbn() == isbn)
                .findFirst()
                .ifPresentOrElse(l -> {
                    l.setIsbn(libro.getIsbn());
                    l.setTitulo(libro.getTitulo());
                    l.setAutor(libro.getAutor());
                    l.setAnio(libro.getAnio());
                    l.setCategoria(libro.getCategoria());
                    l.setEstado(libro.getEstado());
                }, () -> {
                    throw new RuntimeException("No existe un libro con ISBN: " + isbn);
                });

            jsonFileManager.write(FILE_PATH, libros);
        } catch (IOException e) {
            throw new RuntimeException("No se puede actualizar un libro", e);
        }
    }
}
