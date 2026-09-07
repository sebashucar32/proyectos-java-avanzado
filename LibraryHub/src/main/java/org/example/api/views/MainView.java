package org.example.api.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.api.config.GuiConfig;
import org.example.api.views.reportes.ReporteView;

public class MainView {

    private final GuiConfig guiConfig;

    public MainView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }

    public VBox crearVista(Stage primaryStage) {
        Label titulo = new Label("LibraryHub");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button botonLibros = new Button("Gestión de libros");
        Button botonUsuarios = new Button("Gestión de usuarios");
        Button botonPrestamos = new Button("Gestión de préstamos");
        Button botonReportes = new Button("Reportes");
        Button botonSalir = new Button("Salir");

        configurarBoton(botonLibros);
        configurarBoton(botonUsuarios);
        configurarBoton(botonPrestamos);
        configurarBoton(botonReportes);
        configurarBoton(botonSalir);

        botonLibros.setOnAction(event -> {
            LibroView libroView = new LibroView(guiConfig);
            primaryStage.getScene().setRoot(libroView.crearVista(primaryStage));
        });

        botonUsuarios.setOnAction(event -> {
            UsuarioView usuarioView = new UsuarioView(guiConfig);
            primaryStage.getScene().setRoot(usuarioView.crearVista(primaryStage));
        });

        botonPrestamos.setOnAction(event -> {
            PrestamoView prestamoView = new PrestamoView(guiConfig);
            primaryStage.getScene().setRoot(prestamoView.crearVista(primaryStage));
        });

        botonReportes.setOnAction(event -> {
            ReporteView reporteView = new ReporteView(guiConfig);
            primaryStage.getScene().setRoot(reporteView.crearVista(primaryStage));
        });

        botonSalir.setOnAction(event -> Platform.exit());

        VBox vista = new VBox(
            titulo,
            botonLibros,
            botonUsuarios,
            botonPrestamos,
            botonReportes,
            botonSalir
        );

        vista.setAlignment(Pos.CENTER);
        vista.setSpacing(10);
        VBox.setMargin(titulo, new Insets(0, 0, 40, 0));

        return vista;
    }

    private void configurarBoton(Button boton) {
        boton.setPrefWidth(250);
        boton.setPrefHeight(40);
    }
}
