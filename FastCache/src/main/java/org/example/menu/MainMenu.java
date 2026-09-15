package org.example.menu;

import java.util.Scanner;

public class MainMenu {
    private final Scanner teclado;

    public MainMenu(Scanner teclado) {
        this.teclado = teclado;
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> System.out.println("Elemento guardado");
                case 2 -> System.out.println("Elemento obtenido");
                case 3 -> System.out.println("Elemento actualizado");
                case 4 -> System.out.println("Elemento eliminado");
                case 5 -> System.out.println("Verificando la existencia");
                case 6 -> System.out.println("Tamaño de los elementos");
                case 7 -> System.out.println("Elementos vaciados en cache");
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
