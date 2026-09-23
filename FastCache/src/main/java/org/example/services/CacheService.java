package org.example.services;

import org.example.repositories.PoliticaExpulsion;

import java.util.HashMap;
import java.util.Map;

public class CacheService {
    private final int capacidad;
    private final Map<String, String> cache;
    private final PoliticaExpulsion politicaExpulsion;

    public CacheService(PoliticaExpulsion politicaExpulsion) {
        this.politicaExpulsion = politicaExpulsion;
        this.capacidad = 3;
        this.cache = new HashMap<>();
    }

    public void guardar(String clave, String valor) {
        validarClave(clave);
        validarValor(valor);

        if (cache.containsKey(clave)) {
            throw new IllegalArgumentException(
                "La clave '" + clave + "' ya existe en la caché."
            );
        }

        gestionarCapacidad();
        cache.put(clave, valor);
        politicaExpulsion.registrarEntrada(clave);
    }

    public String buscarElemento(String clave) {
        String valor = this.cache.get(clave);

        if (valor == null) {
            throw new IllegalArgumentException(
                "La clave '" + clave + "' no existe en la caché."
            );
        }

        politicaExpulsion.registrarAcceso(clave);
        return valor;
    }

    public void actualizar(String clave, String valor) {
        validarClave(clave);
        validarValor(valor);

        if (!cache.containsKey(clave)) {
            throw new IllegalArgumentException(
                "La clave '" + clave + "' debe existir en la caché."
            );
        }

        cache.put(clave, valor);
    }

    public void eliminar(String clave) {
        validarClave(clave);

        if (!cache.containsKey(clave)) {
            throw new IllegalArgumentException(
                "La clave '" + clave + "' debe existir en la caché."
            );
        }

        this.cache.remove(clave);
        politicaExpulsion.registrarEliminacion(clave);
    }

    public boolean verificarExistencia(String clave) {
        validarClave(clave);
        String valor = this.cache.get(clave);
        return valor != null;
    }

    public int verTamanioCache() {
        return this.cache.size();
    }

    public void vaciarCache() {
        this.cache.clear();
    }

    private void validarClave(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La clave no puede ser nula ni estar vacia");
        }
    }

    private void validarValor(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El valor no puede ser nulo ni estar vacio");
        }
    }

    private void gestionarCapacidad() {
        if (this.cache.size() >= capacidad) {
            String claveAExpulsar = politicaExpulsion.seleccionarClave();
            cache.remove(claveAExpulsar);
            politicaExpulsion.registrarEliminacion(claveAExpulsar);
        }
    }
}
