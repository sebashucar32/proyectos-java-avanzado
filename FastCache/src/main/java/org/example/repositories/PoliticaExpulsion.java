package org.example.repositories;

public interface PoliticaExpulsion<K> {
    K seleccionarClave();
    void registrarEntrada(K clave);
    void registrarEliminacion(K clave);
    void registrarAcceso(K clave);
    void limpiar();
}
