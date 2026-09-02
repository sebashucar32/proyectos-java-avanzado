package org.example.repository;

import org.example.models.Libro;

import java.util.List;
import java.util.function.Function;

public interface LibroRepository {
    void registrar(Libro libro);
    void editar(int id, Libro libro);
    void eliminar(Libro libro, Function<Libro, Integer> idExtractor);
    List<Libro> listar();
}
