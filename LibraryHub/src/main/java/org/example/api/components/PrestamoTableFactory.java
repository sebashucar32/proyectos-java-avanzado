package org.example.api.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.models.Prestamo;

import java.time.format.DateTimeFormatter;

public final class PrestamoTableFactory {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private PrestamoTableFactory() {}

    public static TableView<Prestamo> crear() {
        TableView<Prestamo> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new javafx.scene.control.Label("No hay préstamos registrados"));

        TableColumn<Prestamo, Integer> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaId.setMaxWidth(60);

        TableColumn<Prestamo, String> columnaLibro = new TableColumn<>("Libro");
        columnaLibro.setCellValueFactory(celda -> {
            Prestamo prestamo = celda.getValue();
            if (prestamo.getLibro() == null) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty(prestamo.getLibro().getTitulo());
        });

        TableColumn<Prestamo, String> columnaUsuario = new TableColumn<>("Usuario");
        columnaUsuario.setCellValueFactory(celda -> {
            Prestamo prestamo = celda.getValue();
            if (prestamo.getUsuario() == null) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty(prestamo.getUsuario().getNombre());
        });

        TableColumn<Prestamo, String> columnaInicio = new TableColumn<>("Inicio");
        columnaInicio.setCellValueFactory(celda ->
            new SimpleStringProperty(formatearFecha(celda.getValue().getFechaInicio()))
        );

        TableColumn<Prestamo, String> columnaVencimiento = new TableColumn<>("Vencimiento");
        columnaVencimiento.setCellValueFactory(celda ->
            new SimpleStringProperty(formatearFecha(celda.getValue().getFechaVencimiento()))
        );

        TableColumn<Prestamo, String> columnaDevolucion = new TableColumn<>("Devolución");
        columnaDevolucion.setCellValueFactory(celda -> {
            var fecha = celda.getValue().getFechaDevolucion();
            if (fecha == null) {
                return new SimpleStringProperty("Activo");
            }
            return new SimpleStringProperty(formatearFecha(fecha));
        });

        tabla.getColumns().addAll(
            columnaId,
            columnaLibro,
            columnaUsuario,
            columnaInicio,
            columnaVencimiento,
            columnaDevolucion
        );

        return tabla;
    }

    private static String formatearFecha(java.time.LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FORMATO_FECHA);
    }
}
