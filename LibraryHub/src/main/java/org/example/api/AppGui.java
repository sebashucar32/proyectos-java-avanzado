package org.example.api;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AppGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Crear los componentes visuales
        Button botonEjecutar = new Button("Ejecutar comando CLI");

        // 2. Organizar los componentes en un contenedor layout
        StackPane raiz = new StackPane();
        raiz.getChildren().add(botonEjecutar);

        // 3. Crear la escena definiendo el tamaño de la ventana (Ancho x Alto)
        Scene escena = new Scene(raiz, 500, 400);
        escena.setFill(Color.RED);

        // 4. Configurar el escenario principal (Ventana)
        primaryStage.setTitle("LibraryHub");
        primaryStage.setScene(escena);

        // 5. Mostrar la interfaz grafica
        primaryStage.show();
    }

    public static void lanzarConfiguracionJavaFx(String[] args) {
        launch(args);
    }
}
