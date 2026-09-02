package org.example.service;

import org.example.enums.EstadoLibro;
import org.example.models.Libro;
import org.example.repository.LibroRepository;

import java.util.List;

public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void registrar(int isbn, String titulo, String autor, int anio, String categoria) {
        var nuevoLibro = new Libro();
        nuevoLibro.setIsbn(isbn);
        nuevoLibro.setTitulo(titulo);
        nuevoLibro.setAnio(anio);
        nuevoLibro.setCategoria(categoria);
        nuevoLibro.setAutor(autor);
        nuevoLibro.setEstado(EstadoLibro.DISPONIBLE);

        libroRepository.registrar(nuevoLibro);
        System.out.println(isbn + " " + titulo + " " + autor + " " + anio + " " + categoria);
    }

    public void editar(int isbn, String titulo, String autor, int anio, String categoria, EstadoLibro estadoLibro) {
        var libroEditado = new Libro();
        libroEditado.setIsbn(isbn);
        libroEditado.setTitulo(titulo);
        libroEditado.setAnio(anio);
        libroEditado.setCategoria(categoria);
        libroEditado.setAutor(autor);
        libroEditado.setEstado(estadoLibro);

        libroRepository.editar(isbn, libroEditado);
        System.out.println(isbn + " " + titulo + " " + autor + " " + anio + " " + categoria);
    }

    public void eliminar(int isbn) {
        Libro libro = libroRepository.listar().stream()
            .filter(l -> l.getIsbn() == isbn)
            .findFirst()
            .orElse(null);

        libroRepository.eliminar(libro, Libro::getIsbn);
    }

    public Libro buscarIsbn(int isbn) {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getIsbn().equals(isbn))
            .findFirst()
            .orElse(null);
    }

    public Libro buscarAutor(String autor) {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getAutor().equals(autor))
            .findFirst()
            .orElse(null);
    }

    public Libro buscarCategoria(String categoria) {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getCategoria().equals(categoria))
            .findFirst()
            .orElse(null);
    }

    public Libro buscarTitulo(String titulo) {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getTitulo().equals(titulo))
            .findFirst()
            .orElse(null);
    }

    public List<Libro> obtenerDisponibles() {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE)
            .toList();
    }

    public List<Libro> obtenerPrestados() {
        return libroRepository.listar().stream()
            .filter(libro -> libro.getEstado() == EstadoLibro.PRESTADO)
            .toList();
    }
}
