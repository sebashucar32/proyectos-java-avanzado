package org.example.controllers;

import org.example.enums.EstadoLibro;
import org.example.enums.TipoUsuario;
import org.example.exception.LibroNoDisponibleException;
import org.example.exception.PrestamoNoEncontrado;
import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;
import org.example.service.PrestamoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestamoControllerTest {
    @Mock
    private PrestamoService prestamoService;

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
    void registrarPrestamoCuandoLosDatosSonValidos() {
        PrestamoController controller = controlador("""
            5
            Don Quijote
            Ana
            2026-01-01
            2026-01-15
            """);

        controller.registrarPrestamo();

        verify(prestamoService).registrar(5, "Don Quijote", "Ana",
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 15));
        assertTrue(salida().contains("Préstamo registrado correctamente."));
    }

    @Test
    void registrarPrestamoCuandoElIdNoEsNumerico() {
        PrestamoController controller = controlador("""
            abc
            """);

        controller.registrarPrestamo();

        verify(prestamoService, never()).registrar(anyInt(), anyString(), anyString(), any(), any());
        assertTrue(salida().contains("El id debe ser un número entero."));
    }

    @Test
    void registrarPrestamoCuandoLaFechaEsInvalida() {
        PrestamoController controller = controlador("""
            5
            Don Quijote
            Ana
            01/01/2026
            """);

        controller.registrarPrestamo();

        verify(prestamoService, never()).registrar(anyInt(), anyString(), anyString(), any(), any());
        assertTrue(salida().contains("Formato de fecha inválido. Use yyyy-MM-dd."));
    }

    @Test
    void registrarPrestamoCuandoElLibroNoEstaDisponible() {
        doThrow(new LibroNoDisponibleException("No se pueden prestar libros que ya se encuentren en prestamo"))
            .when(prestamoService).registrar(anyInt(), anyString(), anyString(), any(), any());
        PrestamoController controller = controlador("""
            5
            Don Quijote
            Ana
            2026-01-01
            2026-01-15
            """);

        controller.registrarPrestamo();

        assertTrue(salida().contains("No se pueden prestar libros que ya se encuentren en prestamo"));
    }

    @Test
    void devolverLibroCuandoLosDatosSonValidos() {
        PrestamoController controller = controlador("""
            Ana
            Don Quijote
            """);

        controller.devolverLibro();

        verify(prestamoService).devolver(eq("Ana"), eq("Don Quijote"), any(LocalDate.class));
        assertTrue(salida().contains("Libro devuelto correctamente."));
    }

    @Test
    void devolverLibroCuandoNoExistePrestamo() {
        doThrow(new PrestamoNoEncontrado("No existe un prestamo que tenga un libro y un usuario asociado"))
            .when(prestamoService).devolver(anyString(), anyString(), any());
        PrestamoController controller = controlador("""
            Ana
            Don Quijote
            """);

        controller.devolverLibro();

        assertTrue(salida().contains("No existe un prestamo que tenga un libro y un usuario asociado"));
    }

    @Test
    void renovarLibroCuandoLosDatosSonValidos() {
        PrestamoController controller = controlador("""
            Ana
            Don Quijote
            2026-02-01
            """);

        controller.renovarLibro();

        verify(prestamoService).renovar("Ana", "Don Quijote", LocalDate.of(2026, 2, 1));
        assertTrue(salida().contains("Préstamo renovado correctamente."));
    }

    @Test
    void renovarLibroCuandoLaFechaEsInvalida() {
        PrestamoController controller = controlador("""
            Ana
            Don Quijote
            01-02-2026
            """);

        controller.renovarLibro();

        verify(prestamoService, never()).renovar(anyString(), anyString(), any());
        assertTrue(salida().contains("Formato de fecha inválido. Use yyyy-MM-dd."));
    }

    @Test
    void consultarHistorialPrestamosCuandoHayDatos() {
        when(prestamoService.consultarHistorial()).thenReturn(List.of(crearPrestamo()));

        controlador("").consultarHistorialPrestamos();

        verify(prestamoService).consultarHistorial();
        assertTrue(salida().contains("Don Quijote"));
        assertTrue(salida().contains("Ana"));
    }

    @Test
    void obtenerUsuariosConMultasCuandoHayDatos() {
        when(prestamoService.obtenerMultas()).thenReturn(List.of(new Multa(3, 15000.0, "Ana")));

        controlador("").obtenerUsuariosConMultas();

        verify(prestamoService).obtenerMultas();
        assertTrue(salida().contains("Ana"));
    }

    @Test
    void obtenerPrestamosVencidosCuandoHayDatos() {
        when(prestamoService.obtenerVencidos()).thenReturn(List.of(crearPrestamo()));

        controlador("").obtenerPrestamosVencidos();

        verify(prestamoService).obtenerVencidos();
        assertTrue(salida().contains("Don Quijote"));
    }

    @Test
    void usuarioConMasPrestamosCuandoHayDatos() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Ana");
        usuario.setTipoUsuario(TipoUsuario.Estudiante);
        when(prestamoService.usuarioConMasPrestamos()).thenReturn(List.of(usuario));

        controlador("").usuarioConMasPrestamos();

        verify(prestamoService).usuarioConMasPrestamos();
        assertTrue(salida().contains("Ana"));
    }

    @Test
    void libroMasSolicitadoCuandoHayDatos() {
        when(prestamoService.libroMasSolicitado()).thenReturn(List.of(crearLibro()));

        controlador("").libroMasSolicitado();

        verify(prestamoService).libroMasSolicitado();
        assertTrue(salida().contains("Don Quijote"));
    }

    @Test
    void consultarHistorialPrestamosCuandoElServicioFalla() {
        when(prestamoService.consultarHistorial()).thenThrow(new RuntimeException("Error de persistencia"));

        controlador("").consultarHistorialPrestamos();

        assertTrue(salida().contains("Error de persistencia"));
    }

    private PrestamoController controlador(String entrada) {
        return new PrestamoController(new Scanner(entrada), prestamoService);
    }

    private String salida() {
        return salidaCapturada.toString();
    }

    private static Prestamo crearPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(5);
        prestamo.setLibros(crearLibro());
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Ana");
        usuario.setTipoUsuario(TipoUsuario.Estudiante);
        prestamo.setUsuario(usuario);
        prestamo.setFechaInicio(LocalDate.of(2026, 1, 1));
        prestamo.setFechaVencimiento(LocalDate.of(2026, 1, 15));
        return prestamo;
    }

    private static Libro crearLibro() {
        Libro libro = new Libro();
        libro.setIsbn(10);
        libro.setTitulo("Don Quijote");
        libro.setAutor("Cervantes");
        libro.setAnio(1605);
        libro.setCategoria("Aventura");
        libro.setEstado(EstadoLibro.PRESTADO);
        return libro;
    }
}
