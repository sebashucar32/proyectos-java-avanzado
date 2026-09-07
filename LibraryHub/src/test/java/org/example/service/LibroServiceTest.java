package org.example.service;

import org.example.enums.EstadoLibro;
import org.example.exception.LibroNoEncontrado;
import org.example.models.Libro;
import org.example.repository.JsonLibroRepository;
import org.example.repository.LibroRepository;
import org.example.util.JsonFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibroServiceTest {
    private final JsonFileManager jsonFileManager = new JsonFileManager();
    private final LibroRepository libroRepository = new JsonLibroRepository(jsonFileManager);
    private final LibroService libroService = new LibroService(libroRepository);

    private final List<Integer> isbnsRegistrados = new ArrayList<>();

    @BeforeEach
    void setUp() {
        isbnsRegistrados.clear();
    }

    @AfterEach
    void tearDown() {
        for (int isbn : isbnsRegistrados) {
            try {
                libroService.eliminar(isbn);
            } catch (LibroNoEncontrado ignored) {
            }
        }
    }

    @Test
    void registrarLibroCuandoSeRegitraCorrectamente() {
        int isbn = 99;
        String titulo = "Don quijote de la mancha";
        String autor = "Miguel de Cervantes Saavedra";
        int anio = 1605;
        String categoria = "Aventura";

        libroService.registrar(isbn, titulo, autor, anio, categoria);
        isbnsRegistrados.add(isbn);

        Libro libro = libroService.buscarIsbn(isbn);
        assertEquals(isbn, libro.getIsbn());
        assertEquals(titulo, libro.getTitulo());
        assertEquals(autor, libro.getAutor());
        assertEquals(anio, libro.getAnio());
        assertEquals(categoria, libro.getCategoria());
        assertEquals(EstadoLibro.DISPONIBLE, libro.getEstado());
    }

    @Test
    void editarLibroCuandoSeEditaCorrectamente() {
        int isbn = 99;
        libroService.registrar(isbn, "Don quijote de la mancha", "Miguel de Cervantes Saavedra",
            1605, "Aventura");
        isbnsRegistrados.add(isbn);

        String titulo = "Don Quijote (edicion anotada)";
        String autor = "Miguel de Cervantes";
        int anio = 1615;
        String categoria = "Drama";
        EstadoLibro estadoLibro = EstadoLibro.PRESTADO;

        libroService.editar(isbn, titulo, autor, anio, categoria, estadoLibro);

        Libro libro = libroService.buscarIsbn(isbn);
        assertEquals(isbn, libro.getIsbn());
        assertEquals(titulo, libro.getTitulo());
        assertEquals(autor, libro.getAutor());
        assertEquals(anio, libro.getAnio());
        assertEquals(categoria, libro.getCategoria());
        assertEquals(estadoLibro, libro.getEstado());
    }

    @Test
    void eliminarLibroCuandoSeEliminaCorrectamente() {
        int isbn = 99;
        libroService.registrar(isbn, "Don quijote de la mancha", "Miguel de Cervantes Saavedra",
            1605, "Aventura");

        libroService.eliminar(isbn);

        assertThrows(LibroNoEncontrado.class, () -> libroService.buscarIsbn(isbn));
    }

    @Test
    void eliminarLibroCuandoNoEstaRegistrado() {
        assertThrows(LibroNoEncontrado.class, () -> libroService.eliminar(999999));
    }

    @Test
    void buscarIsbnCuandoNoExiste() {
        assertThrows(LibroNoEncontrado.class, () -> libroService.buscarIsbn(999999));
    }

    @Test
    void buscarAutorCuandoSeEncuentraCorrectamente() {
        int isbn = 99;
        String autor = "Autor de prueba LibraryHub";
        libroService.registrar(isbn, "Titulo de prueba autor", autor, 2020, "Ensayo");
        isbnsRegistrados.add(isbn);

        Libro libro = libroService.buscarAutor(autor);
        assertEquals(isbn, libro.getIsbn());
        assertEquals(autor, libro.getAutor());
    }

    @Test
    void buscarAutorCuandoNoExiste() {
        assertThrows(LibroNoEncontrado.class, () -> libroService.buscarAutor("Autor inexistente LibraryHub"));
    }

    @Test
    void buscarCategoriaCuandoSeEncuentraCorrectamente() {
        int isbn = 99;
        String categoria = "Categoria de prueba LibraryHub";
        libroService.registrar(isbn, "Titulo de prueba categoria", "Autor categoria", 2020, categoria);
        isbnsRegistrados.add(isbn);

        Libro libro = libroService.buscarCategoria(categoria);
        assertEquals(isbn, libro.getIsbn());
        assertEquals(categoria, libro.getCategoria());
    }

    @Test
    void buscarCategoriaCuandoNoExiste() {
        assertThrows(LibroNoEncontrado.class, () -> libroService.buscarCategoria("Categoria inexistente LibraryHub"));
    }

    @Test
    void buscarTituloCuandoSeEncuentraCorrectamente() {
        int isbn = 99;
        String titulo = "Titulo de prueba LibraryHub";
        libroService.registrar(isbn, titulo, "Autor titulo", 2020, "Ensayo");
        isbnsRegistrados.add(isbn);

        Libro libro = libroService.buscarTitulo(titulo);
        assertEquals(isbn, libro.getIsbn());
        assertEquals(titulo, libro.getTitulo());
    }

    @Test
    void buscarTituloCuandoNoExiste() {
        assertThrows(LibroNoEncontrado.class, () -> libroService.buscarTitulo("Titulo inexistente LibraryHub"));
    }

    @Test
    void obtenerDisponiblesCuandoHayLibrosDisponibles() {
        int isbn = 99;
        libroService.registrar(isbn, "Libro disponible de prueba", "Autor disponible", 2020, "Ensayo");
        isbnsRegistrados.add(isbn);

        List<Libro> disponibles = libroService.obtenerDisponibles();

        assertTrue(disponibles.stream().anyMatch(libro -> libro.getIsbn().equals(isbn)));
        assertTrue(disponibles.stream().allMatch(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE));
    }

    @Test
    void obtenerPrestadosCuandoHayLibrosPrestados() {
        int isbn = 99;
        libroService.registrar(isbn, "Libro prestado de prueba", "Autor prestado", 2020, "Ensayo");
        isbnsRegistrados.add(isbn);
        libroService.editar(isbn, "Libro prestado de prueba", "Autor prestado", 2020, "Ensayo", EstadoLibro.PRESTADO);

        List<Libro> prestados = libroService.obtenerPrestados();

        assertTrue(prestados.stream().anyMatch(libro -> libro.getIsbn().equals(isbn)));
        assertTrue(prestados.stream().allMatch(libro -> libro.getEstado() == EstadoLibro.PRESTADO));
    }
}
