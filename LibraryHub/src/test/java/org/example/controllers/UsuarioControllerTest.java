package org.example.controllers;

import org.example.enums.TipoUsuario;
import org.example.exception.UsuarioNoEncontrado;
import org.example.models.Usuario;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {
    @Mock
    private UsuarioService usuarioService;

    private PrintStream salidaOriginal;
    private ByteArrayOutputStream salidaCapturada;

    @BeforeEach
    void setUp() {
        salidaOriginal = System.out;
        salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));
    }

    @AfterEach
    void tearDown() {
        System.setOut(salidaOriginal);
    }

    @Test
    void registrarUsuarioCuandoLosDatosSonValidos() {
        UsuarioController controller = controlador("""
            1
            Ana Perez
            Estudiante
            """);

        controller.registrarUsuario();

        verify(usuarioService).registrar(1, "Ana Perez", TipoUsuario.Estudiante);
        assertTrue(salida().contains("Usuario registrado con id: 1"));
    }

    @Test
    void registrarUsuarioCuandoElIdNoEsNumerico() {
        UsuarioController controller = controlador("""
            abc
            """);

        controller.registrarUsuario();

        verify(usuarioService, never()).registrar(anyInt(), anyString(), any());
        assertTrue(salida().contains("El id debe ser un número entero."));
    }

    @Test
    void registrarUsuarioCuandoElTipoEsInvalido() {
        UsuarioController controller = controlador("""
            1
            Ana Perez
            Admin
            """);

        controller.registrarUsuario();

        verify(usuarioService, never()).registrar(anyInt(), anyString(), any());
        assertTrue(salida().contains("Tipo de usuario inválido. Use Estudiante o Profesor."));
    }

    @Test
    void editarUsuarioCuandoLosDatosSonValidos() {
        UsuarioController controller = controlador("""
            2
            Luis Gomez
            Profesor
            """);

        controller.editarUsuario();

        verify(usuarioService).editar(2, "Luis Gomez", TipoUsuario.Profesor);
        assertTrue(salida().contains("Usuario editado correctamente."));
    }

    @Test
    void eliminarUsuarioCuandoExiste() {
        UsuarioController controller = controlador("7\n");

        controller.eliminarUsuario();

        verify(usuarioService).eliminar(7);
        assertTrue(salida().contains("Usuario eliminado correctamente."));
    }

    @Test
    void eliminarUsuarioCuandoElIdNoEsNumerico() {
        UsuarioController controller = controlador("abc\n");

        controller.eliminarUsuario();

        verify(usuarioService, never()).eliminar(anyInt());
        assertTrue(salida().contains("El id debe ser un número entero."));
    }

    @Test
    void eliminarUsuarioCuandoNoExiste() {
        doThrow(new UsuarioNoEncontrado("Usuario no encontrado: 99"))
            .when(usuarioService).eliminar(99);
        UsuarioController controller = controlador("99\n");

        controller.eliminarUsuario();

        assertTrue(salida().contains("Usuario no encontrado: 99"));
    }

    @Test
    void obtenerUsuariosCuandoElServicioResponde() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Ana");
        usuario.setTipoUsuario(TipoUsuario.Estudiante);
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario));

        List<Usuario> resultado = controlador("").obtenerUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        assertTrue(salida().contains("Ana"));
    }

    @Test
    void obtenerUsuariosCuandoElServicioFalla() {
        when(usuarioService.obtenerTodos()).thenThrow(new RuntimeException("Error de persistencia"));

        List<Usuario> resultado = controlador("").obtenerUsuarios();

        assertTrue(resultado.isEmpty());
        assertTrue(salida().contains("Error de persistencia"));
    }

    private UsuarioController controlador(String entrada) {
        return new UsuarioController(new Scanner(entrada), usuarioService);
    }

    private String salida() {
        return salidaCapturada.toString();
    }
}
