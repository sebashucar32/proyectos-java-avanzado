package org.example.config;

import org.example.controllers.CacheController;
import org.example.menu.MainMenu;
import org.example.policies.FifoPolicy;
import org.example.policies.LfuPolicy;
import org.example.policies.LruPolicy;
import org.example.repositories.PoliticaExpulsion;
import org.example.services.CacheService;

import java.util.Scanner;

public class AppConfig {
    private AppConfig() {}

    public static MainMenu crearMainMenu() {
        Scanner teclado = new Scanner(System.in);

        //PoliticaExpulsion politicaExpulsion = new FifoPolicy();
        //PoliticaExpulsion politicaExpulsion = new LruPolicy();
        PoliticaExpulsion politicaExpulsion = new LfuPolicy();
        CacheService cacheService = new CacheService(politicaExpulsion);
        CacheController cacheController = new CacheController(teclado, cacheService);

        return new MainMenu(teclado, cacheController);
    }
}
