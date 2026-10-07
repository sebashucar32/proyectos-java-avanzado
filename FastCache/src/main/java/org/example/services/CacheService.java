package org.example.services;

import org.example.models.CacheEntry;
import org.example.models.Estadistica;
import org.example.repositories.PoliticaExpulsion;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CacheService<K, V> {
    private final int capacidad;
    private final Map<K, CacheEntry<V>> cache;
    private final PoliticaExpulsion<K> politicaExpulsion;
    private final Estadistica estadistica;

    public CacheService(PoliticaExpulsion<K> politicaExpulsion) {
        this(politicaExpulsion, 3);
    }

    public CacheService(PoliticaExpulsion<K> politicaExpulsion, int capacidad) {
        this.politicaExpulsion = politicaExpulsion;
        this.capacidad = capacidad;
        this.cache = new HashMap<>();
        this.estadistica = new Estadistica(0L, 0L, 0L, 0L, 0L);
    }

    public void guardar(K clave, V valor) {
        validarClave(clave);
        validarValor(valor);

        LocalDateTime tiempoExpirado = LocalDateTime.now().plusSeconds(45);
        CacheEntry<V> valorCache = new CacheEntry<>(valor, tiempoExpirado);

        if (cache.containsKey(clave)) {
            throw new IllegalArgumentException("La clave '" + clave + "' ya existe en la caché.");
        }

        gestionarCapacidad();

        cache.put(clave, valorCache);
        politicaExpulsion.registrarEntrada(clave);
    }

    public V buscarElemento(K clave) {
        validarClave(clave);

        long inicio = System.nanoTime();

        try {
            CacheEntry<V> cacheEntry = this.cache.get(clave);

            if (cacheEntry == null) {
                estadistica.setMisses(estadistica.getMisses() + 1);
                throw new IllegalArgumentException("La clave '" + clave + "' no existe en la caché.");
            }

            if (cacheEntry.estaExpirada()) {
                cache.remove(clave);
                politicaExpulsion.registrarEliminacion(clave);
                estadistica.setMisses(estadistica.getMisses() + 1);
                estadistica.setExpirados(estadistica.getExpirados() + 1);

                throw new IllegalArgumentException("La clave '" + clave + "' ha expirado.");
            }

            politicaExpulsion.registrarAcceso(clave);
            estadistica.setHits(estadistica.getHits() + 1);

            return cacheEntry.getValor();

        } finally {
            long fin = System.nanoTime();
            long tiempoTranscurrido = fin - inicio;
            estadistica.setTiempoAcceso(estadistica.getTiempoAcceso() + tiempoTranscurrido);
        }
    }

    public void actualizar(K clave, V valor) {
        validarClave(clave);
        validarValor(valor);

        LocalDateTime tiempoExpirado = LocalDateTime.now().plusSeconds(45);
        CacheEntry<V> valorCache = new CacheEntry<>(valor, tiempoExpirado);

        if (!cache.containsKey(clave)) {
            throw new IllegalArgumentException("La clave '" + clave + "' debe existir en la caché.");
        }

        cache.put(clave, valorCache);
    }

    public void eliminar(K clave) {
        validarClave(clave);

        if (!cache.containsKey(clave)) {
            throw new IllegalArgumentException(
                "La clave '" + clave + "' debe existir en la caché."
            );
        }

        this.cache.remove(clave);
        estadistica.setExpulsados(estadistica.getExpulsados() + 1);
        politicaExpulsion.registrarEliminacion(clave);
    }

    public boolean verificarExistencia(K clave) {
        validarClave(clave);

        CacheEntry<V> cacheEntry = this.cache.get(clave);

        if (cacheEntry == null) {
            return false;
        }

        if (cacheEntry.estaExpirada()) {
            cache.remove(clave);
            politicaExpulsion.registrarEliminacion(clave);
            estadistica.setExpirados(estadistica.getExpirados() + 1);

            return false;
        }

        return true;
    }

    public int verTamanioCache() {
        limpiarElementosExpirados();

        return this.cache.size();
    }

    public void vaciarCache() {
        this.cache.clear();
        this.politicaExpulsion.limpiar();
    }

    public Estadistica estadisticas() {
        return estadistica;
    }

    private void validarClave(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser nula");
        }
    }

    private void validarValor(V valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
    }

    private void gestionarCapacidad() {
        limpiarElementosExpirados();

        if (this.cache.size() >= capacidad) {
            K claveAExpulsar = politicaExpulsion.seleccionarClave();

            cache.remove(claveAExpulsar);
            estadistica.setExpulsados(estadistica.getExpulsados() + 1);
            politicaExpulsion.registrarEliminacion(claveAExpulsar);
        }
    }

    private void limpiarElementosExpirados() {
        cache.entrySet().removeIf(entry -> {
            if (entry.getValue().estaExpirada()) {
                politicaExpulsion.registrarEliminacion(entry.getKey());
                estadistica.setExpirados(estadistica.getExpirados() + 1);

                return true;
            }

            return false;
        });
    }
}
