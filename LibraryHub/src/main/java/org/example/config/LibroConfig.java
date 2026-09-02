package org.example.config;

import org.example.controllers.LibroController;
import org.example.menu.LibroMenu;
import org.example.repository.JsonLibroRepository;
import org.example.repository.LibroRepository;
import org.example.service.LibroService;
import org.example.util.JsonFileManager;

import java.util.Scanner;

public final class LibroConfig {
    private LibroConfig() {}

    public static LibroMenu crearMenu(Scanner teclado, JsonFileManager jsonFileManager) {
        LibroRepository repository =
            new JsonLibroRepository(jsonFileManager);

        LibroService service =
            new LibroService(repository);

        LibroController controller =
            new LibroController(teclado, service);

        return new LibroMenu(teclado, controller);
    }
}
