package org.example.service;

import org.example.enums.TipoUsuario;
import org.example.exception.UsuarioNoEncontrado;
import org.example.models.Usuario;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarCuandoSeRegistraCorrectamente() {
        usuarioService.registrar(1, "Ana Perez", TipoUsuario.Estudiante);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).registrar(captor.capture());

        Usuario registrado = captor.getValue();
        assertEquals(1, registrado.getId());
        assertEquals("Ana Perez", registrado.getNombre());
        assertEquals(TipoUsuario.Estudiante, registrado.getTipoUsuario());
    }

    @Test
    void editarCuandoSeEditaCorrectamente() {
        usuarioService.editar(1, "Ana Gomez", TipoUsuario.Profesor);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).editar(eq(1), captor.capture());

        Usuario editado = captor.getValue();
        assertEquals(1, editado.getId());
        assertEquals("Ana Gomez", editado.getNombre());
        assertEquals(TipoUsuario.Profesor, editado.getTipoUsuario());
    }

    @Test
    void eliminarCuandoElUsuarioExiste() {
        Usuario usuario = crearUsuario(7, "Carlos", TipoUsuario.Estudiante);
        when(usuarioRepository.listar()).thenReturn(List.of(usuario));

        usuarioService.eliminar(7);

        verify(usuarioRepository).eliminar(eq(usuario), any());
    }

    @Test
    void eliminarCuandoNoEstaRegistrado() {
        when(usuarioRepository.listar()).thenReturn(List.of());

        assertThrows(UsuarioNoEncontrado.class, () -> usuarioService.eliminar(999));
        verify(usuarioRepository, never()).eliminar(any(), any());
    }

    @Test
    void obtenerTodosCuandoHayUsuarios() {
        List<Usuario> esperados = List.of(
            crearUsuario(1, "Ana", TipoUsuario.Estudiante),
            crearUsuario(2, "Luis", TipoUsuario.Profesor)
        );
        when(usuarioRepository.listar()).thenReturn(esperados);

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertEquals(esperados, resultado);
        verify(usuarioRepository).listar();
    }

    private static Usuario crearUsuario(int id, String nombre, TipoUsuario tipoUsuario) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setTipoUsuario(tipoUsuario);
        return usuario;
    }
}
