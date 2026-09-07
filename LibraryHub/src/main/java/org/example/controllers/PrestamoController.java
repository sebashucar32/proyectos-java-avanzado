package org.example.controllers;

import org.example.exception.PrestamoExcepcion;
import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;
import org.example.service.PrestamoService;
import org.example.util.Tabla;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PrestamoController {
    private final Scanner teclado;
    private final PrestamoService prestamoService;

    public PrestamoController(Scanner teclado, PrestamoService prestamoService) {
        this.teclado = teclado;
        this.prestamoService = prestamoService;
    }

    public void registrarPrestamo() {
        try {
            System.out.println("Ingrese el id del prestamo: ");
            int id = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese el libro que desea prestar: ");
            String libros = teclado.nextLine();

            System.out.println("Ingrese el usuario del prestamo: ");
            String usuario = teclado.nextLine();

            System.out.println("Ingrese la fecha de ingreso (yyyy-MM-dd) del prestamo: ");
            LocalDate fechaIngreso = LocalDate.parse(teclado.nextLine());

            System.out.println("Ingrese la fecha de vencimiento (yyyy-MM-dd) del prestamo: ");
            LocalDate fechaVencimiento = LocalDate.parse(teclado.nextLine());

            prestamoService.registrar(id, libros, usuario, fechaIngreso, fechaVencimiento);
            System.out.println("Préstamo registrado correctamente.");
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("El id debe ser un número entero.");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (PrestamoExcepcion e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void devolverLibro() {
        try {
            System.out.println("Ingrese el usuario que hara devolución: ");
            String usuario = teclado.nextLine();

            System.out.println("Ingrese el libro que desea devolver: ");
            String libro = teclado.nextLine();

            LocalDate fechaDevolucion = LocalDate.now();

            prestamoService.devolver(usuario, libro, fechaDevolucion);
            System.out.println("Libro devuelto correctamente.");
        } catch (PrestamoExcepcion e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void renovarLibro() {
        try {
            System.out.println("Ingrese el usuario que hara renovación: ");
            String usuario = teclado.nextLine();

            System.out.println("Ingrese el libro que desea renovar: ");
            String libro = teclado.nextLine();

            System.out.println("Ingrese la fecha de vencimiento (yyyy-MM-dd) para renovar prestamo: ");
            LocalDate fechaVencimiento = LocalDate.parse(teclado.nextLine());

            prestamoService.renovar(usuario, libro, fechaVencimiento);
            System.out.println("Préstamo renovado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (PrestamoExcepcion e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void consultarHistorialPrestamos() {
        try {
            List<Prestamo> historial = prestamoService.consultarHistorial();
            Tabla.imprimirPrestamos(historial);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void obtenerUsuariosConMultas() {
        try {
            List<Multa> multas = prestamoService.obtenerMultas();
            Tabla.imprimirMultas(multas);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void obtenerPrestamosVencidos() {
        try {
            List<Prestamo> prestamos = prestamoService.obtenerVencidos();
            Tabla.imprimirPrestamos(prestamos);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void usuarioConMasPrestamos() {
        try {
            List<Usuario> usuarios = prestamoService.usuarioConMasPrestamos();
            Tabla.imprimirUsuarios(usuarios);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void libroMasSolicitado() {
        try {
            List<Libro> libros = prestamoService.libroMasSolicitado();
            Tabla.imprimirLibros(libros);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    private String mensajeError(RuntimeException e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
