package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.LinkedHashMap;

public class LruPolicy<K> implements PoliticaExpulsion<K> {

    // accessOrder=true: cada get/put mueve la entrada al final automáticamente — O(1)
    private final LinkedHashMap<K, K> orden = new LinkedHashMap<>(16, 0.75f, true);

    @Override
    public K seleccionarClave() {
        // El primer elemento del iterador es el menos recientemente usado
        return orden.keySet().iterator().next();
    }

    @Override
    public void registrarEntrada(K clave) {
        orden.put(clave, clave);
    }

    @Override
    public void registrarEliminacion(K clave) {
        orden.remove(clave);
    }

    @Override
    public void registrarAcceso(K clave) {
        // LinkedHashMap con accessOrder=true mueve la clave al final al hacer get
        orden.get(clave);
    }

    @Override
    public void limpiar() {
        orden.clear();
    }
}
