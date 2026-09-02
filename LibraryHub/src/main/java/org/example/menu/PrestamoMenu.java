package org.example.menu;

import org.example.Main;
import org.example.controllers.PrestamoController;

import java.util.Scanner;

public class PrestamoMenu {
    private final Scanner teclado;
    private final PrestamoController prestamoController;

    public PrestamoMenu(Scanner teclado, PrestamoController prestamoController) {
        this.teclado = teclado;
        this.prestamoController = prestamoController;
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1 -> prestamoController.registrarPrestamo();
                case 2 -> prestamoController.devolverLibro();
                case 3 -> prestamoController.renovarLibro();
                case 0 -> {
                    return;
                }
                default -> System.out.println("No existe la opción marcada");
            }
        } while(true);
    }

    private void mostrarMenu() {
        System.out.println("------ Préstamos ------");
        System.out.println("1. Crear prestamo");
        System.out.println("2. Devolver libro");
        System.out.println("3. Renovar préstamo");
        System.out.println("0. Volver");

        System.out.println("Seleccione una opción:");
    }
}
