package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Prestamo;
import org.example.util.ComunRepository;
import org.example.util.JsonFileManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class JsonPrestamoRepository extends ComunRepository<Prestamo> implements PrestamoRepository {
    private static final String FILE_PATH = "data/Prestamo.json";

    public JsonPrestamoRepository(JsonFileManager jsonFileManager) {
        super(jsonFileManager, "data/Prestamo.json", new TypeReference<>() {});
    }

    @Override
    public void devolver(Prestamo prestamo, LocalDate fechaDevolucion) {
        try {
            List<Prestamo> prestamos = listar();

            prestamos.stream()
                .filter(p -> Objects.equals(p.getId(), prestamo.getId()))
                .findFirst()
                .ifPresentOrElse(pr -> {
                    pr.setId(prestamo.getId());
                    pr.setUsuario(prestamo.getUsuario());
                    pr.setLibros(prestamo.getLibro());
                    pr.setFechaInicio(prestamo.getFechaInicio());
                    pr.setFechaVencimiento(prestamo.getFechaVencimiento());
                    pr.setFechaDevolucion(fechaDevolucion);
                }, () -> {
                    throw new RuntimeException("No existe el prestamo que desea devolver: " + prestamo.getId());
                });

            jsonFileManager.write(FILE_PATH, prestamos);
        } catch (IOException e) {
            throw new RuntimeException("No se puede actualizar el prestamo para su devolución", e);
        }
    }

    @Override
    public void renovar(Prestamo prestamo, LocalDate fechaRenovacion) {
        try {
            List<Prestamo> prestamos = listar();

            prestamos.stream()
                .filter(p -> fechaRenovacion.isAfter(p.getFechaVencimiento())
                    && Objects.equals(p.getId(), prestamo.getId()))
                .filter(pr -> pr.getFechaDevolucion() == null)
                .findFirst()
                .ifPresentOrElse(pr -> {
                    pr.setId(prestamo.getId());
                    pr.setUsuario(prestamo.getUsuario());
                    pr.setLibros(prestamo.getLibro());
                    pr.setFechaInicio(prestamo.getFechaInicio());
                    pr.setFechaVencimiento(fechaRenovacion);
                    pr.setFechaDevolucion(prestamo.getFechaDevolucion());
                }, () -> {
                    throw new RuntimeException("No se puede renovar con la fecha solicitada: " + prestamo.getId());
                });

            jsonFileManager.write(FILE_PATH, prestamos);
        } catch (IOException e) {
            throw new RuntimeException("No se puede actualizar el prestamo para su renovación", e);
        }
    }
}
