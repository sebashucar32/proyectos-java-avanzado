package org.example.repository;

import org.example.models.Prestamo;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepository {
    void registrar(Prestamo prestamo);
    List<Prestamo> listar();
    void devolver(Prestamo prestamo, LocalDate fechaDevolucion);
    void renovar(Prestamo prestamo, LocalDate fechaRenovacion);
}
