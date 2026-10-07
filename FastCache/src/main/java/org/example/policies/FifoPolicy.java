package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.LinkedList;
import java.util.Queue;

public class FifoPolicy<K> implements PoliticaExpulsion<K> {
    Queue<K> cola = new LinkedList<>();

    @Override
    public K seleccionarClave() {
        return cola.peek();   // Obtener el primer elemento de la cola sin eliminarlo.
    }

    @Override
    public void registrarEntrada(K clave) {
        cola.offer(clave);  // agrega un elemento al final de la cola.
    }

    @Override
    public void registrarEliminacion(K clave) {
        cola.remove(clave);   // Elimina de la cola la clave que ya salió de la caché
    }

    @Override
    public void registrarAcceso(K clave) {}

    @Override
    public void limpiar() {
        cola.clear();
    }
}
