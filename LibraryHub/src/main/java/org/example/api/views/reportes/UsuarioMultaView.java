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
import org.example.api.components.MultaTablaFactory;
import org.example.api.config.GuiConfig;
import org.example.api.controllers.ReporteGuiController;
import org.example.records.Multa;

import java.util.List;

public class UsuarioMultaView {
    private final GuiConfig guiConfig;
    private final ReporteGuiController reporteGuiController;

    public UsuarioMultaView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
        this.reporteGuiController = guiConfig.reportes();
    }

    public BorderPane crearVista(Stage primaryStage) {
        List<Multa> multas = reporteGuiController.obtenerMultas();

        Label titulo = new Label("Usuarios con multa");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label resumen = new Label(formatearResumen(multas.size()));
        resumen.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        VBox encabezado = new VBox(titulo, resumen);
        encabezado.setAlignment(Pos.CENTER);
        encabezado.setSpacing(8);
        encabezado.setPadding(new Insets(15, 15, 10, 15));

        TableView<Multa> tabla = MultaTablaFactory.crear();
        tabla.setItems(FXCollections.observableArrayList(multas));
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
            ? "1 multa registrada en el sistema"
            : total + " multas registradas en el sistema";
    }
}
