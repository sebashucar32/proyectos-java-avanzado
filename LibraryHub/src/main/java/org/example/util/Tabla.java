package org.example.util;

import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;

import java.util.List;

public class Tabla {
    public static void imprimirLibros(List<Libro> libros) {
        System.out.printf(
            "%-15s %-30s %-25s %-6s %-20s %-15s%n",
            "ISBN",
            "TÍTULO",
            "AUTOR",
            "AÑO",
            "CATEGORÍA",
            "ESTADO"
        );

        System.out.println("-".repeat(115));

        libros.forEach(libro -> System.out.printf(
            "%-15s %-30s %-25s %-6d %-20s %-15s%n",
            libro.getIsbn(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getAnio(),
            libro.getCategoria(),
            libro.getEstado()
        ));

        System.out.println();
        System.out.println();
    }

    public static void imprimirUsuarios(List<Usuario> usuarios) {
        System.out.printf(
            "%-15s %-30s %-25s%n",
            "ID",
            "NOMBRE",
            "TIPO USUARIO"
        );

        System.out.println("-".repeat(115));

        usuarios.forEach(usuario -> System.out.printf(
            "%-15s %-30s %-25s%n",
            usuario.getId(),
            usuario.getNombre(),
            usuario.getTipoUsuario()
        ));

        System.out.println();
        System.out.println();
    }

    public static void imprimirPrestamos(List<Prestamo> prestamos) {
        System.out.printf(
            "%-15s %-30s %-25s %-6s %-20s %-15s%n",
            "ID",
            "LIBRO",
            "USUARIO",
            "FECHA INICIO",
            "FECHA VENCIMIENTO",
            "FECHA DEVOLUCION"
        );

        System.out.println("-".repeat(115));

        prestamos.forEach(prestamo -> System.out.printf(
            "%-15s %-30s %-25s %-6s %-20s %-15s%n",
            prestamo.getId(),
            prestamo.getLibro().getTitulo(),
            prestamo.getUsuario().getNombre(),
            prestamo.getFechaInicio(),
            prestamo.getFechaVencimiento(),
            prestamo.getFechaDevolucion()
        ));

        System.out.println();
        System.out.println();
    }

    public static void imprimirMultas(List<Multa> multas) {
        System.out.printf(
            "%-15s %-30s %-25s%n",
            "DIAS DE RETRASO",
            "VALOR",
            "NOMBRE DE USUARIO"
        );

        System.out.println("-".repeat(115));

        multas.forEach(multa -> System.out.printf(
            "%-15s %-30s %-25s%n",
            multa.diasRetraso(),
            multa.valor(),
            multa.nombreUsuario()
        ));

        System.out.println();
        System.out.println();
    }
}
