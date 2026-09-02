package org.example.repository;

import org.example.records.Multa;

import java.util.List;

public interface MultaRepository {
    void registrar(Multa multa);
    List<Multa> listar();
}
