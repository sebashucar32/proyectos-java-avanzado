package org.example.menu;

import org.example.Main;
import org.example.controllers.UsuarioController;

import java.util.Scanner;

public class UsuarioMenu {
    private final Scanner teclado;
    private final UsuarioController usuarioController;

    public UsuarioMenu(Scanner teclado, UsuarioController usuarioController) {
        this.teclado = teclado;
        this.usuarioController = usuarioController;
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1 -> usuarioController.registrarUsuario();
                case 2 -> usuarioController.editarUsuario();
                case 3 -> usuarioController.eliminarUsuario();
                case 4 -> usuarioController.obtenerUsuarios();
                case 0 -> {
                    return;
                }
                default -> System.out.println("No existe la opción marcada");
            }
        } while(true);
    }

    private void mostrarMenu() {
        System.out.println("------ Usuarios ------");
        System.out.println("1. Crear usuario");
        System.out.println("2. Editar usuario");
        System.out.println("3. Eliminar usuario");
        System.out.println("4. Buscar usuario");
        System.out.println("0. Volver");

        System.out.println("Seleccione una opción:");
    }
}
