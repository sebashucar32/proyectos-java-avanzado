package org.example.controllers;

import org.example.enums.EstadoLibro;
import org.example.exception.LibroNoEncontrado;
import org.example.models.Libro;
import org.example.service.LibroService;
import org.example.util.Tabla;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LibroController {
    private final Scanner teclado;
    private final LibroService libroService;

    public LibroController(Scanner teclado, LibroService libroService) {
        this.teclado = teclado;
        this.libroService = libroService;
    }

    public void imprimirResultado(Libro libro) {
        System.out.println("Libro encontrado");
        List<Libro> tabla = List.of(libro);
        Tabla.imprimirLibros(tabla);
    }

    public void registrarLibro() {
        try {
            System.out.println("Ingrese el isbn del libro: ");
            int isbn = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese el titulo para el libro: ");
            String titulo = teclado.nextLine();

            System.out.println("Ingrese el autor del libro: ");
            String autor = teclado.nextLine();

            System.out.println("Ingrese el año que se publico el libro: ");
            int anio = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese la categoria a la que pertenece el libro: ");
            String categoria = teclado.nextLine();

            libroService.registrar(isbn, titulo, autor, anio, categoria);
            System.out.println("Libro registrado: " + isbn + " " + titulo + " " + autor + " " + anio + " " + categoria);
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("ISBN y año deben ser números enteros.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void editarLibro() {
        try {
            System.out.println("Ingrese el isbn del libro: ");
            int isbn = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese el titulo para el libro: ");
            String titulo = teclado.nextLine();

            System.out.println("Ingrese el autor del libro: ");
            String autor = teclado.nextLine();

            System.out.println("Ingrese el año que se publico el libro: ");
            int anio = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese la categoria a la que pertenece el libro: ");
            String categoria = teclado.nextLine();

            System.out.println("Ingrese el estado que desea cambiar el libro (DISPONIBLE, PRESTADO): ");
            EstadoLibro estadoLibro = EstadoLibro.valueOf(teclado.nextLine());

            libroService.editar(isbn, titulo, autor, anio, categoria, estadoLibro);
            System.out.println("Libro editado: " + isbn + " " + titulo + " " + autor + " " + anio + " " + categoria);
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("ISBN y año deben ser números enteros.");
        } catch (IllegalArgumentException e) {
            System.out.println("Estado inválido. Use DISPONIBLE o PRESTADO.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void eliminarLibro() {
        try {
            System.out.println("Ingrese el isbn que desea eliminar: ");
            int isbn = Integer.parseInt(teclado.nextLine());

            libroService.eliminar(isbn);
            System.out.println("Libro eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("El ISBN debe ser un número entero.");
        } catch (LibroNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void buscarLibroPorIsbn() {
        try {
            System.out.println("Ingrese el isbn que desea buscar: ");
            int isbn = Integer.parseInt(teclado.nextLine());
            Libro libro = libroService.buscarIsbn(isbn);
            imprimirResultado(libro);
        } catch (NumberFormatException e) {
            System.out.println("El ISBN debe ser un número entero.");
        } catch (LibroNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void buscarLibroPorAutor() {
        try {
            System.out.println("Ingrese el autor que desea buscar: ");
            String autor = teclado.nextLine();
            Libro libro = libroService.buscarAutor(autor);
            imprimirResultado(libro);
        } catch (LibroNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void buscarLibroPorCategoria() {
        try {
            System.out.println("Ingrese la categoria que desea buscar: ");
            String categoria = teclado.nextLine();
            Libro libro = libroService.buscarCategoria(categoria);
            imprimirResultado(libro);
        } catch (LibroNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void buscarLibroPorTitulo() {
        try {
            System.out.println("Ingrese el titulo que desea buscar: ");
            String titulo = teclado.nextLine();
            Libro libro = libroService.buscarTitulo(titulo);
            imprimirResultado(libro);
        } catch (LibroNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public List<Libro> obtenerLibrosDisponibles() {
        try {
            List<Libro> libros = libroService.obtenerDisponibles();
            Tabla.imprimirLibros(libros);
            return libros;
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
            return List.of();
        }
    }

    public List<Libro> obtenerLibrosPrestados() {
        try {
            List<Libro> libros = libroService.obtenerPrestados();
            Tabla.imprimirLibros(libros);
            return libros;
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
            return List.of();
        }
    }

    private String mensajeError(RuntimeException e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
