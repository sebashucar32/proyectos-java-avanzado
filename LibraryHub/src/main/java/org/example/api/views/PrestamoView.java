package org.example.api.views;

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

public class PrestamoView {

    private final GuiConfig guiConfig;

    public PrestamoView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }

    public VBox crearVista(Stage primaryStage) {
        Label titulo = new Label("Gestión de préstamos");
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button botonCrear = new Button("Crear prestamo");
        Button botonDevolver = new Button("Devolver libro");
        Button botonRenovar = new Button("Renovar préstamo");
        Button botonVolver = new Button("Volver");

        botonCrear.setPrefWidth(250);
        botonDevolver.setPrefWidth(250);
        botonRenovar.setPrefWidth(250);
        botonVolver.setPrefWidth(250);

        botonCrear.setPrefHeight(40);
        botonDevolver.setPrefHeight(40);
        botonRenovar.setPrefHeight(40);
        botonVolver.setPrefHeight(40);

        botonVolver.setOnAction(event -> {
            MainView mainView = new MainView(guiConfig);
            primaryStage.getScene().setRoot(mainView.crearVista(primaryStage));
        });

        VBox vista = new VBox();

        vista.getChildren().addAll(
                titulo,
                botonCrear,
                botonDevolver,
                botonRenovar,
                botonVolver
        );

        vista.setAlignment(Pos.CENTER);
        vista.setSpacing(10);
        VBox.setMargin(titulo, new Insets(0, 0, 40, 0));

        return vista;
    }
}
