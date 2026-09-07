package org.example.api.views.reportes;

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
import org.example.api.views.MainView;
import org.example.api.views.reportes.CantidadPrestamosView;

public class ReporteView {

    private final GuiConfig guiConfig;

    public ReporteView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }

    public VBox crearVista(Stage primaryStage) {
        Label titulo = new Label("Reportes");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button botonLibroSolicitado = new Button("Libro mas solicitado");
        Button botonUsuarioPrestamo = new Button("Usuario con mas prestamos");
        Button botonDisponibles = new Button("Libros disponibles");
        Button botonPrestados = new Button("Libros prestados");
        Button botonPrestamoVencido = new Button("Prestamos vencidos");
        Button botonUsuarioMulta = new Button("Usuarios con multas");
        Button botonCantidadPrestamo = new Button("Cantidad total de prestamos");
        Button botonVolver = new Button("Volver");

        configurarBoton(botonLibroSolicitado);
        configurarBoton(botonUsuarioPrestamo);
        configurarBoton(botonDisponibles);
        configurarBoton(botonPrestados);
        configurarBoton(botonPrestamoVencido);
        configurarBoton(botonUsuarioMulta);
        configurarBoton(botonCantidadPrestamo);
        configurarBoton(botonVolver);

        botonUsuarioMulta.setOnAction(event -> {
            UsuarioMultaView usuarioMultaView = new UsuarioMultaView(guiConfig);
            primaryStage.getScene().setRoot(usuarioMultaView.crearVista(primaryStage));
        });

        botonCantidadPrestamo.setOnAction(event -> {
            CantidadPrestamosView cantidadView = new CantidadPrestamosView(guiConfig);
            primaryStage.getScene().setRoot(cantidadView.crearVista(primaryStage));
        });

        botonVolver.setOnAction(event -> {
            MainView mainView = new MainView(guiConfig);
            primaryStage.getScene().setRoot(mainView.crearVista(primaryStage));
        });

        VBox vista = new VBox(
            titulo,
            botonLibroSolicitado,
            botonUsuarioPrestamo,
            botonDisponibles,
            botonPrestados,
            botonPrestamoVencido,
            botonUsuarioMulta,
            botonCantidadPrestamo,
            botonVolver
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
