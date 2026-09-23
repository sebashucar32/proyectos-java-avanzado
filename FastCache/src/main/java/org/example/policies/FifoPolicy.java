package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.LinkedList;
import java.util.Queue;

public class FifoPolicy implements PoliticaExpulsion {
    Queue<String> cola = new LinkedList<>();

    @Override
    public String seleccionarClave() {
        return cola.peek();   // Obtener el primer elemento de la cola sin eliminarlo.
    }

    @Override
    public void registrarEntrada(String clave) {
        cola.offer(clave);  // agrega un elemento al final de la cola.
    }

    @Override
    public void registrarEliminacion(String clave) {
        cola.remove(clave);   // Elimina de la cola la clave que ya salió de la caché
    }

    @Override
    public void registrarAcceso(String clave) {}
}
