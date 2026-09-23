package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.LinkedList;
import java.util.Queue;

public class LruPolicy implements PoliticaExpulsion {
    Queue<String> cola = new LinkedList<>();

    @Override
    public String seleccionarClave() {
        return cola.peek();
    }

    @Override
    public void registrarEntrada(String clave) {
        cola.offer(clave);
    }

    @Override
    public void registrarEliminacion(String clave) {
        cola.remove(clave);
    }

    @Override
    public void registrarAcceso(String clave) {
        registrarEliminacion(clave);
        registrarEntrada(clave);
    }
}
