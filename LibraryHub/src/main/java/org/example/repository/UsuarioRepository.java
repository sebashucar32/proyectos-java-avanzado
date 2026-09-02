package org.example.repository;

import org.example.models.Usuario;

import java.util.List;
import java.util.function.Function;

public interface UsuarioRepository {
    void registrar(Usuario usuario);
    void editar(int id, Usuario usuario);
    void eliminar(Usuario usuario, Function<Usuario, Integer> idExtractor);
    List<Usuario> listar();
}
