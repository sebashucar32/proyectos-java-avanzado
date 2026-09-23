package org.example.policies;

import org.example.repositories.PoliticaExpulsion;

import java.util.*;

public class LfuPolicy implements PoliticaExpulsion {
    Map<String, Integer> repetidos = new LinkedHashMap<>();

    @Override
    public String seleccionarClave() {
        return Collections.min(
            repetidos.entrySet(),
            Map.Entry.comparingByValue()
        ).getKey();
    }

    @Override
    public void registrarEntrada(String clave) {
        repetidos.put(clave, 0);
    }

    @Override
    public void registrarEliminacion(String clave) {
        repetidos.remove(clave);
    }

    @Override
    public void registrarAcceso(String clave) {
        var valorClave = repetidos.get(clave);
        var contadorClave = valorClave + 1;
        repetidos.put(clave, contadorClave);
    }
}
