package org.example.api.config;

import org.example.api.controllers.ReporteGuiController;
import org.example.repository.JsonLibroRepository;
import org.example.repository.JsonMultaRepository;
import org.example.repository.JsonPrestamoRepository;
import org.example.repository.JsonUsuarioRepository;
import org.example.repository.LibroRepository;
import org.example.repository.MultaRepository;
import org.example.repository.PrestamoRepository;
import org.example.repository.UsuarioRepository;
import org.example.service.PrestamoService;
import org.example.util.JsonFileManager;

public final class GuiConfig {
    private final ReporteGuiController reporteGuiController;

    public GuiConfig() {
        JsonFileManager jsonFileManager = new JsonFileManager();

        UsuarioRepository usuarioRepository = new JsonUsuarioRepository(jsonFileManager);
        LibroRepository libroRepository = new JsonLibroRepository(jsonFileManager);
        MultaRepository multaRepository = new JsonMultaRepository(jsonFileManager);
        PrestamoRepository prestamoRepository = new JsonPrestamoRepository(jsonFileManager);

        PrestamoService prestamoService = new PrestamoService(
            usuarioRepository,
            libroRepository,
            multaRepository,
            prestamoRepository
        );

        reporteGuiController = new ReporteGuiController(prestamoService);
    }

    public ReporteGuiController reportes() {
        return reporteGuiController;
    }
}
