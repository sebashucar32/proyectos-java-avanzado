package org.example;

import org.example.config.AppConfig;
import org.example.menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        MainMenu menu = AppConfig.crearMainMenu();
        menu.iniciar();
    }
}
