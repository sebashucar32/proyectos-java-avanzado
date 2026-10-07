package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LfuPolicy<K> implements PoliticaExpulsion<K> {

    // Frecuencia mínima actual — permite seleccionarClave() en O(1)
    private int minFrecuencia = 0;

    // clave → frecuencia de acceso
    private final Map<K, Integer> frecuencias = new HashMap<>();

    // frecuencia → claves con esa frecuencia, en orden de inserción (desempate LRU)
    private final Map<Integer, LinkedHashSet<K>> grupos = new HashMap<>();

    @Override
    public K seleccionarClave() {
        // La clave a expulsar es la primera del grupo con menor frecuencia — O(1)
        return grupos.get(minFrecuencia).iterator().next();
    }

    @Override
    public void registrarEntrada(K clave) {
        frecuencias.put(clave, 1);
        grupos.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(clave);
        minFrecuencia = 1; // una clave recién insertada siempre tiene frecuencia 1
    }

    @Override
    public void registrarEliminacion(K clave) {
        Integer freq = frecuencias.remove(clave);
        if (freq != null) {
            grupos.get(freq).remove(clave);
        }
    }

    @Override
    public void registrarAcceso(K clave) {
        int freq = frecuencias.get(clave);
        frecuencias.put(clave, freq + 1);

        // Mover la clave del grupo actual al siguiente
        grupos.get(freq).remove(clave);
        grupos.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(clave);

        // Si el grupo de la frecuencia mínima quedó vacío, el mínimo sube en 1
        if (grupos.get(minFrecuencia).isEmpty()) {
            minFrecuencia++;
        }
    }

    @Override
    public void limpiar() {
        frecuencias.clear();
        grupos.clear();
        minFrecuencia = 0;
    }
}
