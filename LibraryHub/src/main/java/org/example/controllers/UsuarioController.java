package org.example.controllers;

import org.example.enums.TipoUsuario;

import org.example.models.Usuario;
import org.example.service.UsuarioService;
import org.example.util.Tabla;

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
        int id;
        String nombre;
        TipoUsuario tipoUsuario;

        System.out.println("Ingrese el id del usuario: ");
        id = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el nombre de usuario: ");
        nombre = teclado.nextLine();

        System.out.println("Ingrese el tipo de usuario: ");
        tipoUsuario = TipoUsuario.valueOf(teclado.nextLine());

        usuarioService.registrar(id, nombre, tipoUsuario);
    }

    public void editarUsuario() {
        int id;
        String nombre;
        TipoUsuario tipoUsuario;

        System.out.println("Ingrese el id del usuario a editar: ");
        id = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el nombre del usuario: ");
        nombre = teclado.nextLine();

        System.out.println("Ingrese el tipo de usuario: ");
        tipoUsuario = TipoUsuario.valueOf(teclado.nextLine());

        usuarioService.editar(id, nombre, tipoUsuario);
    }

    public void eliminarUsuario() {
        System.out.println("Ingrese el id que desea eliminar: ");
        int id = Integer.parseInt(teclado.nextLine());

        usuarioService.eliminar(id);
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        Tabla.imprimirUsuarios(usuarios);

        return usuarios;
    }
}
