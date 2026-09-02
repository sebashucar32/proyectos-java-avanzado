package org.example.api;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AppGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Label titulo = new Label("LibraryHub");
        titulo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Button gestorLibros = new Button("Gestión de libros");
        Button gestorUsuarios = new Button("Gestión de usuarios");
        Button gestorPrestamos = new Button("Gestión de préstamos");
        Button reportes = new Button("Reportes");
        Button salir = new Button("Salir");

        gestorLibros.setPrefWidth(250);
        gestorUsuarios.setPrefWidth(250);
        gestorPrestamos.setPrefWidth(250);
        reportes.setPrefWidth(250);
        salir.setPrefWidth(250);

        gestorLibros.setPrefHeight(40);
        gestorUsuarios.setPrefHeight(40);
        gestorPrestamos.setPrefHeight(40);
        reportes.setPrefHeight(40);
        salir.setPrefHeight(40);

        VBox raiz = new VBox();

        raiz.getChildren().addAll(
            titulo,
            gestorLibros,
            gestorUsuarios,
            gestorPrestamos,
            reportes,
            salir
        );;

        // Alinear elementos al centro
        raiz.setAlignment(Pos.CENTER);

        // Espacio entre los botones
        raiz.setSpacing(10);

        // Espacio adicional debajo del título
        VBox.setMargin(titulo, new Insets(0, 0, 40, 0));

        // Escena
        Scene escena = new Scene(raiz, 500, 400);

        primaryStage.setTitle("LibraryHub");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public static void lanzarConfiguracionJavaFx(String[] args) {
        launch(args);
    }
}
