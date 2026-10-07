package org.example.controllers;

import org.example.models.Estadistica;
import org.example.services.CacheService;

import java.util.Scanner;

public class CacheController {
    private final Scanner teclado;
    private final CacheService<String, String> cacheService;

    public CacheController(Scanner teclado, CacheService<String, String> cacheService) {
        this.teclado = teclado;
        this.cacheService = cacheService;
    }

    public void guardarCache() {
        try {
            System.out.println("Ingrese clave: ");
            String clave = teclado.nextLine();

            System.out.println("Ingrese valor: ");
            String valor = teclado.nextLine();

            cacheService.guardar(clave, valor);

            System.out.println("Elemento guardado correctamente.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void buscarElementoCache() {
        try {
            System.out.println("Ingrese clave: ");
            String clave = teclado.nextLine();

            String valor = cacheService.buscarElemento(clave);

            System.out.println("Valor encontrado: " + valor);
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void actualizarElementoCache() {
        try {
            System.out.println("Ingrese clave: ");
            String clave = teclado.nextLine();

            System.out.println("Ingrese valor: ");
            String valor = teclado.nextLine();

            cacheService.actualizar(clave, valor);

            System.out.println("Elemento guardado correctamente.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void eliminarElementoCache() {
        try {
            System.out.println("Ingrese clave: ");
            String clave = teclado.nextLine();

            cacheService.eliminar(clave);

            System.out.println("Elemento eliminado correctamente.");
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void verificarExistenciaCache() {
        try {
            System.out.println("Ingrese clave: ");
            String clave = teclado.nextLine();

            boolean existe = cacheService.verificarExistencia(clave);

            if (existe) {
                System.out.println("El elemento que desea buscar existe en la cache");
            } else {
                System.out.println("No existe el elemento solicitado");
            }
        } catch (RuntimeException e) {
            System.out.println(mensajeError(e));
        }
    }

    public void tamanioElementosCache() {
        var tamanio = this.cacheService.verTamanioCache();

        if (tamanio > 0) {
            System.out.println("Total de elementos en cache: " + tamanio);
        } else {
            System.out.println("No existen elementos en cache en este momento");
        }
    }

    public void vaciarElementosCache() {
        this.cacheService.vaciarCache();
        System.out.println("Se ha limpiado toda la cache disponible");
    }

    public void estadisticas() {
        Estadistica estadistica = cacheService.estadisticas();

        System.out.println("=============== Estadisticas ===============");
        System.out.println("Hits: " + estadistica.getHits());
        System.out.println("Misses: " + estadistica.getMisses());
        System.out.println("Hit Ratio: " + estadistica.getHitRatio());
        System.out.println("Tiempo promedio: "
                + estadistica.getTiempoPromedio() + " ns");
        System.out.println("Expulsados: " + estadistica.getExpulsados());
        System.out.println("Expirados: " + estadistica.getExpirados());
    }

    private String mensajeError(RuntimeException e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
