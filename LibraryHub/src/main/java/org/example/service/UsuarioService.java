package org.example.service;

import org.example.enums.TipoUsuario;
import org.example.exception.UsuarioNoEncontrado;
import org.example.models.Usuario;
import org.example.repository.UsuarioRepository;

import java.util.List;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(int id, String nombre, TipoUsuario tipoUsuario) {
        var nuevoUsuario = new Usuario();

        nuevoUsuario.setId(id);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setTipoUsuario(tipoUsuario);

        usuarioRepository.registrar(nuevoUsuario);
    }

    public void editar(int id, String nombre, TipoUsuario tipoUsuario) {
        var usuarioEditado = new Usuario();

        usuarioEditado.setId(id);
        usuarioEditado.setNombre(nombre);
        usuarioEditado.setTipoUsuario(tipoUsuario);

        usuarioRepository.editar(id, usuarioEditado);
    }

    public void eliminar(int id) {
        Usuario usuario = usuarioRepository.listar().stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + id));

        usuarioRepository.eliminar(usuario, Usuario::getId);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.listar();
    }
}
