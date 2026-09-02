package org.example.controllers;

import org.example.enums.EstadoLibro;
import org.example.models.Libro;
import org.example.records.Multa;
import org.example.service.LibroService;
import org.example.util.Tabla;

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
        int isbn;
        String titulo;
        String autor;
        int anio;
        String categoria;

        System.out.println("Ingrese el isbn del libro: ");
        isbn = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el titulo para el libro: ");
        titulo = teclado.nextLine();

        System.out.println("Ingrese el autor del libro: ");
        autor = teclado.nextLine();

        System.out.println("Ingrese el año que se publico el libro: ");
        anio = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese la categoria a la que pertenece el libro: ");
        categoria = teclado.nextLine();

        libroService.registrar(isbn, titulo, autor, anio, categoria);
    }

    public void editarLibro() {
        int isbn;
        String titulo;
        String autor;
        int anio;
        String categoria;
        EstadoLibro estadoLibro;

        System.out.println("Ingrese el isbn del libro: ");
        isbn = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el titulo para el libro: ");
        titulo = teclado.nextLine();

        System.out.println("Ingrese el autor del libro: ");
        autor = teclado.nextLine();

        System.out.println("Ingrese el año que se publico el libro: ");
        anio = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese la categoria a la que pertenece el libro: ");
        categoria = teclado.nextLine();

        System.out.println("Ingrese el estado que desea cambiar el libro: ");
        estadoLibro = EstadoLibro.valueOf(teclado.nextLine());

        libroService.editar(isbn, titulo, autor, anio, categoria, estadoLibro);
    }

    public void eliminarLibro() {
        System.out.println("Ingrese el isbn que desea eliminar: ");
        int isbn = Integer.parseInt(teclado.nextLine());

        libroService.eliminar(isbn);
    }

    public void buscarLibroPorIsbn() {
        System.out.println("Ingrese el isbn que desea buscar: ");
        String isbn = teclado.nextLine();
        Libro libro = libroService.buscarIsbn(Integer.parseInt(isbn));
        imprimirResultado(libro);
    }

    public void buscarLibroPorAutor() {
        System.out.println("Ingrese el autor que desea buscar: ");
        String autor = teclado.nextLine();
        Libro libro = libroService.buscarAutor(autor);
        imprimirResultado(libro);
    }

    public void buscarLibroPorCategoria() {
        System.out.println("Ingrese la categoria que desea buscar: ");
        String categoria = teclado.nextLine();
        Libro libro = libroService.buscarCategoria(categoria);
        imprimirResultado(libro);
    }

    public void buscarLibroPorTitulo() {
        System.out.println("Ingrese el titulo que desea buscar: ");
        String titulo = teclado.nextLine();
        Libro libro = libroService.buscarTitulo(titulo);
        imprimirResultado(libro);
    }

    public List<Libro> obtenerLibrosDisponibles() {
        List<Libro> libros = libroService.obtenerDisponibles();
        Tabla.imprimirLibros(libros);

        return libros;
    }

    public List<Libro> obtenerLibrosPrestados() {
        List<Libro> libros = libroService.obtenerPrestados();
        Tabla.imprimirLibros(libros);

        return libros;
    }


}
