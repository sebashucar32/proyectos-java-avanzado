package org.example.config;

import org.example.controllers.UsuarioController;
import org.example.menu.UsuarioMenu;
import org.example.repository.JsonUsuarioRepository;
import org.example.repository.UsuarioRepository;
import org.example.service.UsuarioService;
import org.example.util.JsonFileManager;

import java.util.Scanner;

public class UsuarioConfig {
    private UsuarioConfig() {}

    public static UsuarioMenu crearMenu(Scanner teclado, JsonFileManager jsonFileManager) {
        UsuarioRepository repository =
            new JsonUsuarioRepository(jsonFileManager);

        UsuarioService service = new UsuarioService(repository);

        UsuarioController controller = new UsuarioController(teclado, service);

        return new UsuarioMenu(teclado, controller);
    }
}
