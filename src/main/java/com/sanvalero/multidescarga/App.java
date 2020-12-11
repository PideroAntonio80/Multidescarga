package com.sanvalero.multidescarga;

import com.sanvalero.multidescarga.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Creado por @ author: Pedro Orós
 * el 22/11/2020
 */
public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("multidescarga.fxml"));
        loader.setController(new AppController());
        ScrollPane scrollPane = loader.load();

        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();*/

        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("splashmultidescarga.fxml"));
        loader.setController(new SplashController());
        VBox vBox = loader.load();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
