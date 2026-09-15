package org.example;

import org.example.api.AppGui;
import org.example.config.AppConfig;
import org.example.menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--gui")) {
            AppGui.lanzarConfiguracionJavaFx(args);
        } else {
            MainMenu menu = AppConfig.crearMainMenu();
            menu.iniciar();
        }
    }
}
