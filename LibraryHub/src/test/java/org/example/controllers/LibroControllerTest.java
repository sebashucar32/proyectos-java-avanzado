package org.example.controllers;

import org.example.enums.EstadoLibro;
import org.example.exception.LibroNoEncontrado;
import org.example.models.Libro;
import org.example.service.LibroService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {
    @Mock
    private LibroService libroService;

    private PrintStream salidaOriginal;
    private ByteArrayOutputStream salidaCapturada;

    @BeforeEach
    void setUp() {
        salidaOriginal = System.out;
        salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));
    }

    @AfterEach
    void tearDown() {
        System.setOut(salidaOriginal);
    }

    @Test
    void registrarLibroCuandoLosDatosSonValidos() {
        LibroController controller = controlador("""
            99
            Don Quijote
            Cervantes
            1605
            Aventura
            """);

        controller.registrarLibro();

        verify(libroService).registrar(99, "Don Quijote", "Cervantes", 1605, "Aventura");
        assertTrue(salida().contains("Libro registrado: 99 Don Quijote Cervantes 1605 Aventura"));
    }

    @Test
    void registrarLibroCuandoElIsbnNoEsNumerico() {
        LibroController controller = controlador("""
            abc
            """);

        controller.registrarLibro();

        verify(libroService, never()).registrar(anyInt(), anyString(), anyString(), anyInt(), anyString());
        assertTrue(salida().contains("ISBN y año deben ser números enteros."));
    }

    @Test
    void editarLibroCuandoLosDatosSonValidos() {
        LibroController controller = controlador("""
            99
            Don Quijote
            Cervantes
            1615
            Drama
            PRESTADO
            """);

        controller.editarLibro();

        verify(libroService).editar(99, "Don Quijote", "Cervantes", 1615, "Drama", EstadoLibro.PRESTADO);
        assertTrue(salida().contains("Libro editado: 99 Don Quijote Cervantes 1615 Drama"));
    }

    @Test
    void editarLibroCuandoElEstadoEsInvalido() {
        LibroController controller = controlador("""
            99
            Don Quijote
            Cervantes
            1615
            Drama
            PERDIDO
            """);

        controller.editarLibro();

        verify(libroService, never()).editar(anyInt(), anyString(), anyString(), anyInt(), anyString(), any());
        assertTrue(salida().contains("Estado inválido. Use DISPONIBLE o PRESTADO."));
    }

    @Test
    void eliminarLibroCuandoExiste() {
        LibroController controller = controlador("99\n");

        controller.eliminarLibro();

        verify(libroService).eliminar(99);
        assertTrue(salida().contains("Libro eliminado correctamente."));
    }

    @Test
    void eliminarLibroCuandoNoExiste() {
        doThrow(new LibroNoEncontrado("Libro no encontrado no esta registrado"))
            .when(libroService).eliminar(99);
        LibroController controller = controlador("99\n");

        controller.eliminarLibro();

        assertTrue(salida().contains("Libro no encontrado no esta registrado"));
    }

    @Test
    void buscarLibroPorIsbnCuandoExiste() {
        when(libroService.buscarIsbn(99)).thenReturn(crearLibro());
        LibroController controller = controlador("99\n");

        controller.buscarLibroPorIsbn();

        assertTrue(salida().contains("Libro encontrado"));
        assertTrue(salida().contains("Don Quijote"));
    }

    @Test
    void buscarLibroPorIsbnCuandoElIsbnNoEsNumerico() {
        LibroController controller = controlador("abc\n");

        controller.buscarLibroPorIsbn();

        verify(libroService, never()).buscarIsbn(anyInt());
        assertTrue(salida().contains("El ISBN debe ser un número entero."));
    }

    @Test
    void buscarLibroPorAutorCuandoExiste() {
        when(libroService.buscarAutor("Cervantes")).thenReturn(crearLibro());
        LibroController controller = controlador("Cervantes\n");

        controller.buscarLibroPorAutor();

        verify(libroService).buscarAutor("Cervantes");
        assertTrue(salida().contains("Libro encontrado"));
    }

    @Test
    void buscarLibroPorCategoriaCuandoExiste() {
        when(libroService.buscarCategoria("Aventura")).thenReturn(crearLibro());
        LibroController controller = controlador("Aventura\n");

        controller.buscarLibroPorCategoria();

        verify(libroService).buscarCategoria("Aventura");
        assertTrue(salida().contains("Libro encontrado"));
    }

    @Test
    void buscarLibroPorTituloCuandoExiste() {
        when(libroService.buscarTitulo("Don Quijote")).thenReturn(crearLibro());
        LibroController controller = controlador("Don Quijote\n");

        controller.buscarLibroPorTitulo();

        verify(libroService).buscarTitulo("Don Quijote");
        assertTrue(salida().contains("Libro encontrado"));
    }

    @Test
    void obtenerLibrosDisponiblesCuandoElServicioResponde() {
        when(libroService.obtenerDisponibles()).thenReturn(List.of(crearLibro()));

        List<Libro> resultado = controlador("").obtenerLibrosDisponibles();

        assertEquals(1, resultado.size());
        assertTrue(salida().contains("Don Quijote"));
    }

    @Test
    void obtenerLibrosPrestadosCuandoElServicioFalla() {
        when(libroService.obtenerPrestados()).thenThrow(new RuntimeException("Error de persistencia"));

        List<Libro> resultado = controlador("").obtenerLibrosPrestados();

        assertTrue(resultado.isEmpty());
        assertTrue(salida().contains("Error de persistencia"));
    }

    private LibroController controlador(String entrada) {
        return new LibroController(new Scanner(entrada), libroService);
    }

    private String salida() {
        return salidaCapturada.toString();
    }

    private static Libro crearLibro() {
        Libro libro = new Libro();
        libro.setIsbn(99);
        libro.setTitulo("Don Quijote");
        libro.setAutor("Cervantes");
        libro.setAnio(1605);
        libro.setCategoria("Aventura");
        libro.setEstado(EstadoLibro.DISPONIBLE);
        return libro;
    }
}
