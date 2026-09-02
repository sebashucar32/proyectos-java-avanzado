package org.example.menu;

import org.example.controllers.LibroController;

import java.util.Scanner;

public class LibroMenu {
    private final Scanner teclado;
    private final LibroController libroController;

    public LibroMenu(Scanner teclado, LibroController libroController) {
        this.teclado = teclado;
        this.libroController = libroController;
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1 -> libroController.registrarLibro();
                case 2 -> libroController.editarLibro();
                case 3 -> libroController.eliminarLibro();
                case 4 -> libroController.buscarLibroPorIsbn();
                case 5 -> libroController.buscarLibroPorAutor();
                case 6 -> libroController.buscarLibroPorCategoria();
                case 7 -> libroController.buscarLibroPorTitulo();
                case 0 -> {   // Con esa estructura nunca se acumularán llamadas y no tendrás riesgo de StackOverflowError.
                    return;
                }
                default -> System.out.println("No existe la opción marcada");
            }
        } while(true);
    }

    private void mostrarMenu() {
        System.out.println("------ Gestión de libros ------");
        System.out.println("1. Registrar");
        System.out.println("2. Editar");
        System.out.println("3. Eliminar");
        System.out.println("4. Buscar por ISBN");
        System.out.println("5. Buscar por autor");
        System.out.println("6. Buscar por categoría");
        System.out.println("7. Buscar por título");
        System.out.println("0. Volver");

        System.out.println("Seleccione una opción:");
    }
}
