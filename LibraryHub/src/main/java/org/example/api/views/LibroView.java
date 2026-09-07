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

public class LibroView {

    private final GuiConfig guiConfig;

    public LibroView(GuiConfig guiConfig) {
        this.guiConfig = guiConfig;
    }

    public VBox crearVista(Stage primaryStage) {
        Label titulo = new Label("Gestión de libros");
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button botonRegistrar = new Button("Registrar");
        Button botonEditar = new Button("Editar");
        Button botonEliminar = new Button("Eliminar");
        Button botonIsbn = new Button("Buscar por ISBN");
        Button botonAutor = new Button("Buscar por autor");
        Button botonCategoria = new Button("Buscar por categoría");
        Button botontitulo = new Button("Buscar por título");
        Button botonVolver = new Button("Volver");

        botonRegistrar.setPrefWidth(250);
        botonEditar.setPrefWidth(250);
        botonEliminar.setPrefWidth(250);
        botonIsbn.setPrefWidth(250);
        botonAutor.setPrefWidth(250);
        botonCategoria.setPrefWidth(250);
        botontitulo.setPrefWidth(250);
        botonVolver.setPrefWidth(250);

        botonRegistrar.setPrefHeight(40);
        botonEditar.setPrefHeight(40);
        botonEliminar.setPrefHeight(40);
        botonIsbn.setPrefHeight(40);
        botonAutor.setPrefHeight(40);
        botonCategoria.setPrefHeight(40);
        botontitulo.setPrefHeight(40);
        botonVolver.setPrefHeight(40);

        botonVolver.setOnAction(event -> {
            MainView mainView = new MainView(guiConfig);
            primaryStage.getScene().setRoot(mainView.crearVista(primaryStage));
        });

        VBox vista = new VBox();

        vista.getChildren().addAll(
            titulo,
            botonRegistrar,
            botonEditar,
            botonEliminar,
            botonIsbn,
            botonAutor,
            botonCategoria,
            botontitulo,
            botonVolver
        );

        vista.setAlignment(Pos.CENTER);
        vista.setSpacing(10);
        VBox.setMargin(titulo, new Insets(0, 0, 40, 0));

        return vista;
    }
}
