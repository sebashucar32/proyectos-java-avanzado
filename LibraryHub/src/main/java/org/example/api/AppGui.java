package org.example.api;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.api.config.GuiConfig;
import org.example.api.views.MainView;

public class AppGui extends Application {

    @Override
    public void start(Stage primaryStage) {
        GuiConfig guiConfig = new GuiConfig();
        MainView mainView = new MainView(guiConfig);

        Scene escena = new Scene(
            mainView.crearVista(primaryStage),
            500,
            500
        );

        primaryStage.setTitle("LibraryHub");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public static void lanzarConfiguracionJavaFx(String[] args) {
        launch(args);
    }
}
