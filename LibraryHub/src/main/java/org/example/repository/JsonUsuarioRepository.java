package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Usuario;
import org.example.util.ComunRepository;
import org.example.util.JsonFileManager;

import java.io.IOException;
import java.util.List;

public class JsonUsuarioRepository extends ComunRepository<Usuario> implements UsuarioRepository {
    private static final String FILE_PATH = "data/Usuario.json";

    public JsonUsuarioRepository(JsonFileManager jsonFileManager) {
        super(jsonFileManager,"data/Usuario.json", new TypeReference<>() {});
    }

    @Override
    public void editar(int id, Usuario usuario) {
        try {
            List<Usuario> usuarios = listar();

            usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .ifPresentOrElse(us -> {
                    us.setId(usuario.getId());
                    us.setNombre(usuario.getNombre());
                    us.setTipoUsuario(usuario.getTipoUsuario());
                }, () -> {
                    throw new RuntimeException("No existe un usuario con id: " + id);
                });

            jsonFileManager.write(FILE_PATH, usuarios);
        } catch (IOException e) {
            throw new RuntimeException("No se puede actualizar un usuario", e);
        }
    }
}
