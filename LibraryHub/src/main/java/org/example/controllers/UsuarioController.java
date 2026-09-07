package org.example.controllers;

import org.example.enums.TipoUsuario;
import org.example.exception.UsuarioNoEncontrado;
import org.example.models.Usuario;
import org.example.service.UsuarioService;
import org.example.util.Tabla;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UsuarioController {
    private final Scanner teclado;
    private final UsuarioService usuarioService;

    public UsuarioController(Scanner teclado, UsuarioService usuarioService) {
        this.teclado = teclado;
        this.usuarioService = usuarioService;
    }

    public void imprimirResultado(Usuario usuario) {
        System.out.println("Usuario encontrado");
        List<Usuario> tabla = List.of(usuario);
        Tabla.imprimirUsuarios(tabla);
    }

    public void registrarUsuario() {
        try {
            System.out.println("Ingrese el id del usuario: ");
            int id = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese el nombre de usuario: ");
            String nombre = teclado.nextLine();

            System.out.println("Ingrese el tipo de usuario (Estudiante, Profesor): ");
            TipoUsuario tipoUsuario = TipoUsuario.valueOf(teclado.nextLine());

            usuarioService.registrar(id, nombre, tipoUsuario);
            System.out.println("Usuario registrado con id: " + id + " y con nombre " + nombre);
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("El id debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de usuario inválido. Use Estudiante o Profesor.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void editarUsuario() {
        try {
            System.out.println("Ingrese el id del usuario a editar: ");
            int id = teclado.nextInt();
            teclado.nextLine();

            System.out.println("Ingrese el nombre del usuario: ");
            String nombre = teclado.nextLine();

            System.out.println("Ingrese el tipo de usuario (Estudiante, Profesor): ");
            TipoUsuario tipoUsuario = TipoUsuario.valueOf(teclado.nextLine());

            usuarioService.editar(id, nombre, tipoUsuario);
            System.out.println("Usuario editado correctamente.");
        } catch (InputMismatchException e) {
            teclado.nextLine();
            System.out.println("El id debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de usuario inválido. Use Estudiante o Profesor.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void eliminarUsuario() {
        try {
            System.out.println("Ingrese el id que desea eliminar: ");
            int id = Integer.parseInt(teclado.nextLine());

            usuarioService.eliminar(id);
            System.out.println("Usuario eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("El id debe ser un número entero.");
        } catch (UsuarioNoEncontrado e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public List<Usuario> obtenerUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            Tabla.imprimirUsuarios(usuarios);
            return usuarios;
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
            return List.of();
        }
    }

    private String mensajeError(RuntimeException e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
