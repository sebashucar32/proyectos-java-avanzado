package org.example.api.controllers;

import org.example.models.Prestamo;
import org.example.records.Multa;
import org.example.service.PrestamoService;

import java.util.List;

/**
 * Adaptador de reportes para JavaFX.
 * No usa Scanner ni System.out: solo delega al service y devuelve datos para la vista.
 */
public class ReporteGuiController {

    private final PrestamoService prestamoService;

    public ReporteGuiController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    public List<Prestamo> obtenerHistorialPrestamos() {
        return prestamoService.consultarHistorial();
    }

    public List<Multa> obtenerMultas() {
        return prestamoService.obtenerMultas();
    }
}
