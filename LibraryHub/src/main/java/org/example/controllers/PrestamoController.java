package org.example.controllers;

import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;
import org.example.service.PrestamoService;
import org.example.util.Tabla;

import java.time.LocalDate;
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
        int id;
        String libros;
        String usuario;
        LocalDate fechaIngreso;
        LocalDate fechaVencimiento;

        System.out.println("Ingrese el id del prestamo: ");
        id = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el libro que desea prestar: ");
        libros = teclado.nextLine();

        System.out.println("Ingrese el usuario del prestamo: ");
        usuario = teclado.nextLine();

        System.out.println("Ingrese la fecha de ingreso (yyyy-MM-dd) del prestamo: ");
        fechaIngreso = LocalDate.parse(teclado.nextLine());

        System.out.println("Ingrese la fecha de vencimiento (yyyy-MM-dd) del prestamo: ");
        fechaVencimiento = LocalDate.parse(teclado.nextLine());

        prestamoService.registrar(id, libros, usuario, fechaIngreso, fechaVencimiento);
    }

    public void devolverLibro() {
        String usuario;
        String libro;
        LocalDate fechaDevolucion;

        System.out.println("Ingrese el usuario que hara devolución: ");
        usuario = teclado.nextLine();

        System.out.println("Ingrese el libro que desea devolver: ");
        libro = teclado.nextLine();

        fechaDevolucion = LocalDate.now();

        prestamoService.devolver(usuario, libro, fechaDevolucion);
    }

    public void renovarLibro() {
        String usuario;
        String libro;
        LocalDate fechaVencimiento;

        System.out.println("Ingrese el usuario que hara renovación: ");
        usuario = teclado.nextLine();

        System.out.println("Ingrese el libro que desea renovar: ");
        libro = teclado.nextLine();

        System.out.println("Ingrese la fecha de vencimiento (yyyy-MM-dd) para renovar prestamo: ");
        fechaVencimiento = LocalDate.parse(teclado.nextLine());

        prestamoService.renovar(usuario, libro, fechaVencimiento);
    }

    public void consultarHistorialPrestamos() {
        List<Prestamo> historial = prestamoService.consultarhistorial();
        Tabla.imprimirPrestamos(historial);
    }

    public void obtenerUsuariosConMultas() {
        List<Multa> multas = prestamoService.obtenerMultas();
        Tabla.imprimirMultas(multas);
    }

    public void obtenerPrestamosVencidos() {
        List<Prestamo> prestamos = prestamoService.obtenerVencidos();
        Tabla.imprimirPrestamos(prestamos);
    }

    public void usuarioConMasPrestamos() {
        List<Usuario> usuarios = prestamoService.usuarioConMasPrestamos();
        Tabla.imprimirUsuarios(usuarios);
    }

    public void libroMasSolicitado() {
        List<Libro> libros = prestamoService.libroMasSolicitado();
        Tabla.imprimirLibros(libros);
    }
}
