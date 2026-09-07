package org.example.config;

import org.example.controllers.LibroController;
import org.example.controllers.PrestamoController;
import org.example.menu.ReporteMenu;
import org.example.repository.*;
import org.example.service.LibroService;
import org.example.service.PrestamoService;
import org.example.util.JsonFileManager;

import java.util.Scanner;

public class ReporteConfig {
    private ReporteConfig() {}

    public static ReporteMenu crearMenu(Scanner teclado, JsonFileManager jsonFileManager) {
        LibroRepository repository = new JsonLibroRepository(jsonFileManager);
        LibroService service = new LibroService(repository);

        UsuarioRepository usuarioRepository = new JsonUsuarioRepository(jsonFileManager);

        MultaRepository multaRepository = new JsonMultaRepository(jsonFileManager);

        PrestamoRepository prestamoRepository = new JsonPrestamoRepository(jsonFileManager);
        PrestamoService prestamoService = new PrestamoService(usuarioRepository,
            repository, multaRepository, prestamoRepository);

        LibroController controller = new LibroController(teclado, service);
        PrestamoController prestamoController = new PrestamoController(teclado, prestamoService);

        return new ReporteMenu(teclado, controller, prestamoController);
    }
}
