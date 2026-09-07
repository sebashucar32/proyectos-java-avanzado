package org.example.api.views.reportes;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.api.components.PrestamoTableFactory;
import org.example.api.config.GuiConfig;
import org.example.api.controllers.ReporteGuiController;
import org.example.models.Prestamo;

import java.util.List;

public class CantidadPrestamosView {

    private final GuiConfig guiConfig;
    private final ReporteGuiController reporteGuiController;

    public CantidadPrestamosView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
        this.reporteGuiController = guiConfig.reportes();
    }

    public BorderPane crearVista(Stage primaryStage) {
        List<Prestamo> prestamos = reporteGuiController.obtenerHistorialPrestamos();

        Label titulo = new Label("Cantidad total de préstamos");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label resumen = new Label(formatearResumen(prestamos.size()));
        resumen.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        VBox encabezado = new VBox(titulo, resumen);
        encabezado.setAlignment(Pos.CENTER);
        encabezado.setSpacing(8);
        encabezado.setPadding(new Insets(15, 15, 10, 15));

        TableView<Prestamo> tabla = PrestamoTableFactory.crear();
        tabla.setItems(FXCollections.observableArrayList(prestamos));
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button botonVolver = new Button("Volver");
        botonVolver.setPrefWidth(120);
        botonVolver.setOnAction(event -> {
            primaryStage.setWidth(500);
            primaryStage.setHeight(500);
            ReporteView reporteView = new ReporteView(guiConfig);
            primaryStage.getScene().setRoot(reporteView.crearVista(primaryStage));
        });

        VBox pie = new VBox(botonVolver);
        pie.setAlignment(Pos.CENTER);
        pie.setPadding(new Insets(10, 15, 15, 15));

        VBox centro = new VBox(tabla);
        centro.setPadding(new Insets(0, 15, 0, 15));
        VBox.setVgrow(tabla, Priority.ALWAYS);

        BorderPane vista = new BorderPane();
        vista.setTop(encabezado);
        vista.setCenter(centro);
        vista.setBottom(pie);

        primaryStage.setWidth(900);
        primaryStage.setHeight(550);

        return vista;
    }

    private String formatearResumen(int total) {
        return total == 1
            ? "1 préstamo registrado en el sistema"
            : total + " préstamos registrados en el sistema";
    }
}
