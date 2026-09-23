package org.example.repositories;

import java.util.Map;

public interface PoliticaExpulsion {
    String seleccionarClave();
    void registrarEntrada(String clave);
    void registrarEliminacion(String clave);
    void registrarAcceso(String clave);
}
