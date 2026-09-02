package org.example.config;

import org.example.controllers.PrestamoController;
import org.example.menu.PrestamoMenu;
import org.example.repository.*;
import org.example.service.PrestamoService;
import org.example.util.JsonFileManager;

import java.util.Scanner;

public final class PrestamoConfig {
    private PrestamoConfig() {}

    public static PrestamoMenu crearMenu(Scanner teclado, JsonFileManager jsonFileManager) {
        UsuarioRepository usuarioRepository = new JsonUsuarioRepository(jsonFileManager);
        LibroRepository libroRepository = new JsonLibroRepository(jsonFileManager);
        MultaRepository multaRepository = new JsonMultaRepository(jsonFileManager);
        PrestamoRepository repository = new JsonPrestamoRepository(jsonFileManager);
        PrestamoService service = new PrestamoService(usuarioRepository, libroRepository, multaRepository, repository);
        PrestamoController controller = new PrestamoController(teclado, service);

        return new PrestamoMenu(teclado, controller);
    }
}
