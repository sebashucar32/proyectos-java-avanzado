package org.example.models;

import java.time.LocalDateTime;

public class CacheEntry<V> {
    private final V valor;
    private final LocalDateTime tiempoExpiracion;

    public CacheEntry(V valor, LocalDateTime tiempoExpiracion) {
        this.valor = valor;
        this.tiempoExpiracion = tiempoExpiracion;
    }

    public V getValor() {
        return valor;
    }

    public LocalDateTime getTiempoExpiracion() {
        return tiempoExpiracion;
    }

    public boolean estaExpirada() {
        return !LocalDateTime.now().isBefore(tiempoExpiracion);
    }
}
