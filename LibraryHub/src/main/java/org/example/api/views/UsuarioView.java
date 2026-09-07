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

public class UsuarioView {

    private final GuiConfig guiConfig;

    public UsuarioView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }

    public VBox crearVista(Stage primaryStage) {
        Label titulo = new Label("Gestión de usuarios");
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button botonCrear = new Button("Crear usuario");
        Button botonEditar = new Button("Editar usuario");
        Button botonEliminar = new Button("Eliminar usuario");
        Button botonBuscar = new Button("Buscar usuario");
        Button botonVolver = new Button("Volver");

        botonCrear.setPrefWidth(250);
        botonEditar.setPrefWidth(250);
        botonEliminar.setPrefWidth(250);
        botonBuscar.setPrefWidth(250);
        botonVolver.setPrefWidth(250);

        botonCrear.setPrefHeight(40);
        botonEditar.setPrefHeight(40);
        botonEliminar.setPrefHeight(40);
        botonBuscar.setPrefHeight(40);
        botonVolver.setPrefHeight(40);

        botonVolver.setOnAction(event -> {
            MainView mainView = new MainView(guiConfig);
            primaryStage.getScene().setRoot(mainView.crearVista(primaryStage));
        });

        VBox vista = new VBox();

        vista.getChildren().addAll(
            titulo,
            botonCrear,
            botonEditar,
            botonEliminar,
            botonBuscar,
            botonVolver
        );

        vista.setAlignment(Pos.CENTER);
        vista.setSpacing(10);
        VBox.setMargin(titulo, new Insets(0, 0, 40, 0));

        return vista;
    }
}
