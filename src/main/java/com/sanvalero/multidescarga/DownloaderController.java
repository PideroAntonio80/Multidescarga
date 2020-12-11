package com.sanvalero.multidescarga;

import com.sanvalero.multidescarga.util.AlertUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Creado por @ author: Pedro Orós
 * el 22/11/2020
 */
public class DownloaderController implements Initializable {

    public VBox vbLayoutSon;
    public TextField tfDescarga;
    public TextField tfFileName;
    public ProgressBar pbBarra;
    public Label lProgress;
    public Label lNumeroDescarga;
    public Label lFileName;
    public CheckBox chbEligeRuta;
    private String urlText;
    private String ubicacion;
    private DownloadTask downloadTask;
    private int contador;

    private static final Logger logger = LogManager.getLogger(DownloaderController.class);

    public DownloaderController(String urlText, String ubicacion, int contador) {
        logger.trace("Descarga " + urlText + " creada");
        this.urlText = urlText;
        this.ubicacion = ubicacion;
        this.contador = contador;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (lNumeroDescarga != null) {
            lNumeroDescarga.setText(String.valueOf(contador));
        }
        tfDescarga.setText(urlText);
        if(!ubicacion.equals("")) {
            lFileName.setText("Elige Nombre y Extensión de Fichero");
        }
        else {
            lFileName.setText("No has configurado directorio de descarga. Lo elegirás al pulsar Start");
            tfFileName.setEditable(false);
        }
    }

    @FXML
    public void start() {
        File file = null;
        try {
            if(chbEligeRuta.isSelected() || ubicacion.equals("")) {
                FileChooser fileChooser = new FileChooser();
                file = fileChooser.showSaveDialog(tfDescarga.getScene().getWindow());
            } else if(!chbEligeRuta.isSelected() && !ubicacion.equals("")) {
                String filePath = ubicacion + File.separator + tfFileName.getText();
                file = new File(filePath);
            }
            logger.trace("Descarga " + urlText + " iniciada");

            downloadTask = new DownloadTask(urlText, file);
            //pbBarra.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

            pbBarra.progressProperty().unbind();
            pbBarra.progressProperty().bind(downloadTask.progressProperty());

            /*La interface de debajo lleva por parámetros el valor de lo que la tarea está haciendo, el estado anterior y el nuevo

            downloadTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State newState) {
                    if(newState == Worker.State.SUCCEEDED) {
                        AlertUtils.mostrarInformacion("La descarga ha finalizado");
                    }
                }
            });    <--- Esto que está comentado se podría sustituir por el lambda de debajo
            downloadTask.stateProperty().addListener((observableValue, state, newState) -> {
                if(newState == Worker.State.SUCCEEDED) {
                    AlertUtils.mostrarInformacion("La descarga ha finalizado");
                }
            });*/

            downloadTask.stateProperty().addListener((observableValue, state, newState) -> {
                if(newState == Worker.State.SUCCEEDED) {
                    AlertUtils.mostrarInformacion("La descarga ha finalizado");
                }
            });

            downloadTask.messageProperty().addListener((observableValue, oldValue, newValue) -> lProgress.setText(newValue));

            new Thread(downloadTask).start();

        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            logger.error("URL mal formada", murle.fillInStackTrace());
        }

    }

    @FXML
    public void stop() {
        logger.trace("Descarga " + urlText + " detenida");
        if(downloadTask != null) downloadTask.cancel();
    }

    @FXML
    public void erase(){
        logger.trace("Descarga " + urlText + " eliminada");
        vbLayoutSon.getChildren().clear();
    }

    public void stopAll() {
        logger.trace("Todas las descargas detenidas");
        if(downloadTask != null) downloadTask.cancel();
    }
}
