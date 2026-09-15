package org.example.config;

import org.example.menu.MainMenu;

import java.util.Scanner;

public class AppConfig {
    private AppConfig() {}

    public static MainMenu crearMainMenu() {
        Scanner teclado = new Scanner(System.in);
        return new MainMenu(teclado);
    }
}
