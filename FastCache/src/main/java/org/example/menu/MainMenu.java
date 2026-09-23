package org.example.menu;

import org.example.controllers.CacheController;

import java.util.Scanner;

public class MainMenu {
    private final Scanner teclado;
    private final CacheController cacheController;

    public MainMenu(Scanner teclado, CacheController cacheController) {
        this.teclado = teclado;
        this.cacheController = cacheController;
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> cacheController.guardarCache();
                case 2 -> cacheController.buscarElementoCache();
                case 3 -> cacheController.actualizarElementoCache();
                case 4 -> cacheController.eliminarElementoCache();
                case 5 -> cacheController.verificarExistenciaCache();
                case 6 -> cacheController.tamanioElementosCache();
                case 7 -> cacheController.vaciarElementosCache();
                case 8 -> System.out.println("Estadistica de los elementos");
                case 9 -> salir = true;
                default -> System.out.println("No existe la opción marcada");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("========== FASTCACHE =========");
        System.out.println("1. Guardar elemento");
        System.out.println("2. Obtener elemento");
        System.out.println("3. Actualizar elemento");
        System.out.println("4. Eliminar elemento");
        System.out.println("5. Verificar existencia");
        System.out.println("6. Ver tamaño");
        System.out.println("7. Vaciar caché");
        System.out.println("8. Ver estadísticas");
        System.out.println("9. Salir");
        System.out.println("Seleccione una opción:");
    }
}
