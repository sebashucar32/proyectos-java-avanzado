package org.example.config;

import org.example.menu.*;
import org.example.util.JsonFileManager;

import java.util.Scanner;

public class AppConfig {
    private AppConfig() {}

    public static MainMenu crearMainMenu() {
        Scanner teclado = new Scanner(System.in);

        JsonFileManager jsonFileManager = new JsonFileManager();

        LibroMenu libroMenu = LibroConfig.crearMenu(teclado, jsonFileManager);
        UsuarioMenu usuarioMenu = UsuarioConfig.crearMenu(teclado, jsonFileManager);
        PrestamoMenu prestamoMenu = PrestamoConfig.crearMenu(teclado, jsonFileManager);
        ReporteMenu reporteMenu = ReporteConfig.crearMenu(teclado, jsonFileManager);

        return new MainMenu(teclado, libroMenu, usuarioMenu, prestamoMenu, reporteMenu);
    }
}
