package com.sanvalero.multidescarga;

import com.sanvalero.multidescarga.util.R;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Creado por @ author: Pedro Orós
 * el 11/12/2020
 */
public class SplashController implements Initializable {

    public VBox vbSplash;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), vbSplash);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage splashStage = (Stage) vbSplash.getScene().getWindow();
                    splashStage.hide();

                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(R.getUI("multidescarga.fxml"));
                    loader.setController(new AppController());
                    ScrollPane scrollPane = null;

                    scrollPane = loader.load();
                    Scene scene = new Scene(scrollPane);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }
}
