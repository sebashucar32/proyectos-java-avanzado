package org.example.menu;

import org.example.controllers.LibroController;
import org.example.controllers.PrestamoController;

import java.util.Scanner;

public class ReporteMenu {
    private final Scanner teclado;
    private final LibroController libroController;
    private final PrestamoController prestamoController;

    public ReporteMenu(Scanner teclado, LibroController libroController, PrestamoController prestamoController) {
        this.teclado = teclado;
        this.libroController = libroController;
        this.prestamoController = prestamoController;
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1 -> prestamoController.libroMasSolicitado();
                case 2 -> prestamoController.usuarioConMasPrestamos();
                case 3 -> libroController.obtenerLibrosDisponibles();
                case 4 -> libroController.obtenerLibrosPrestados();
                case 5 -> prestamoController.obtenerPrestamosVencidos();
                case 6 -> prestamoController.obtenerUsuariosConMultas();
                case 7 -> prestamoController.consultarHistorialPrestamos();
                case 0 -> {
                    return;
                }
                default -> System.out.println("No existe la opción marcada");
            }
        } while(true);
    }

    private void mostrarMenu() {
        System.out.println("------ Reportes ------");
        System.out.println("1. Libro mas solicitado");
        System.out.println("2. Usuario con mas prestamos");
        System.out.println("3. Libros disponibles");
        System.out.println("4. Libros prestados");
        System.out.println("5. Prestamos vencidos");
        System.out.println("6. Usuarios con multas");
        System.out.println("7. Cantidad total de prestamos");
        System.out.println("0. Volver");

        System.out.println("Seleccione una opción:");
    }
}
