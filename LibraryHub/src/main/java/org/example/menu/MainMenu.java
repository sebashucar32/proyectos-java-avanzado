package org.example.menu;

import java.util.Scanner;

public class MainMenu {
    private final Scanner teclado;
    private final LibroMenu libroMenu;
    private final UsuarioMenu usuarioMenu;
    private final PrestamoMenu prestamoMenu;
    private final ReporteMenu reporteMenu;

    public MainMenu(Scanner teclado, LibroMenu libroMenu, UsuarioMenu usuarioMenu,
                    PrestamoMenu prestamoMenu, ReporteMenu reporteMenu) {
        this.teclado = teclado;
        this.libroMenu = libroMenu;
        this.usuarioMenu = usuarioMenu;
        this.prestamoMenu = prestamoMenu;
        this.reporteMenu = reporteMenu;
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> libroMenu.iniciar();
                case 2 -> usuarioMenu.iniciar();
                case 3 -> prestamoMenu.iniciar();
                case 4 -> reporteMenu.iniciar();
                case 5 -> salir = true;
                default -> System.out.println("No existe la opción marcada");
            }
        }

        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println("========== LIBRARY HUB =========");
        System.out.println("Seleccione una opción para continuar:");
        System.out.println("1. Gestión de libros");
        System.out.println("2. Gestión de usuarios");
        System.out.println("3. Gestión de préstamos");
        System.out.println("4. Reportes");
        System.out.println("5. Salir");
        System.out.println("Seleccione una opción:");
    }
}