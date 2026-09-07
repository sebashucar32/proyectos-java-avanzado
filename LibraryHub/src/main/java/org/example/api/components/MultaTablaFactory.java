package org.example.api.components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.records.Multa;

public final class MultaTablaFactory {

    private MultaTablaFactory() {}

    public static TableView<Multa> crear() {
        TableView<Multa> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new javafx.scene.control.Label("No hay multas registradas"));

        TableColumn<Multa, Number> columnaDias = new TableColumn<>("Días de retraso");
        columnaDias.setCellValueFactory(celda ->
            new SimpleLongProperty(celda.getValue().diasRetraso())
        );

        TableColumn<Multa, Number> columnaValor = new TableColumn<>("Valor");
        columnaValor.setCellValueFactory(celda ->
            new SimpleDoubleProperty(celda.getValue().valor())
        );

        TableColumn<Multa, String> columnaNombreUsuario = new TableColumn<>("Usuario");
        columnaNombreUsuario.setCellValueFactory(celda ->
            new SimpleStringProperty(celda.getValue().nombreUsuario())
        );

        tabla.getColumns().addAll(
            columnaNombreUsuario,
            columnaDias,
            columnaValor
        );

        return tabla;
    }
}
