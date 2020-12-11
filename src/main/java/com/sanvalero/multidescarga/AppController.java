package com.sanvalero.multidescarga;

import com.sanvalero.multidescarga.util.AlertUtils;
import com.sanvalero.multidescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Creado por @ author: Pedro Orós
 * el 22/11/2020
 */
public class AppController implements Initializable {

    public TextField tfUrl;
    public Label lnombreUbicacion;
    public TextField tfMaxDescargas;
    public TextArea taLogger;
    public Button btEliminarTodo;
    public VBox panel;
    public VBox vbLayoutDownlader;
    private List<DownloaderController> downloads;
    private int contador = 1;
    private int contadorMaxDescargas = 0;

    private static final Logger logger = LogManager.getLogger(AppController.class);

    public AppController() {
        downloads = new ArrayList<>();
        logger.trace("Aplicacion abierta");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btEliminarTodo.setDisable(true);
        //readLogger();
        //readerLog();

    }

    @FXML
    public void launchDownload(ActionEvent event) {
        if(tfUrl.getText().equals("")) {
            AlertUtils.mostrarError("Debes introducir una url");
            return;
        }

        contadorMaxDescargas++;

        if(!tfMaxDescargas.getText().equals("") && contadorMaxDescargas > Integer.parseInt(tfMaxDescargas.getText())) {
            AlertUtils.mostrarError("Ha superado el numero maximo de descargas");
            return;
        }

        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();

        launchDownload(urlText);
    }

    @FXML
    public void stopAllDownloads() {
        for (DownloaderController downloaderController : downloads) {
            downloaderController.stopAll();
            btEliminarTodo.setDisable(false);
        }
        logger.trace("Todas las descargas son detenidas");
        //readLogger();
        //readerLog();
    }

    @FXML
    public void eraseAll() {
        vbLayoutDownlader.getChildren().clear();
        btEliminarTodo.setDisable(true);
        contadorMaxDescargas = 0;
        logger.trace("Todas las descargas son eliminadas");
        //readLogger();
        //readerLog();
    }

    @FXML
    public void seleccionaUbicacion(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File fileDir = directoryChooser.showDialog(null);
        String directorio = fileDir.getAbsolutePath();
        lnombreUbicacion.setText(directorio);
        logger.trace("Configurada ruta de descarga");
        //readLogger();
        //readerLog();
    }

    @FXML
    public void btBorrarSeleccion() {
        lnombreUbicacion.setText("");
        logger.trace("Eliminada ruta de descarga configurada");
        //readLogger();
        //readerLog();
    }

    @FXML
    public void readFrom() {
        FileChooser fileChooser = new FileChooser();
        File file =fileChooser.showOpenDialog(null);
        try {
            List<String> urls = Files.readAllLines(file.toPath());
            urls.forEach(this::launchDownload);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("Error al leer el archivo", ioe.fillInStackTrace());
            //readLogger();
            //readerLog();
        }
    }

    public void launchDownload(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("downloader.fxml"));
            DownloaderController downloaderController = new DownloaderController(url, lnombreUbicacion.getText(), contador);
            loader.setController(downloaderController);
            VBox downloader = loader.load();

            vbLayoutDownlader.getChildren().add(downloader);

            downloads.add(downloaderController);
            contador++;
            //readLogger();
            //readerLog();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    /*public void readLogger() {  TODO ESTE LEE Y MUESTRA EL .LOG PERO ME CUELGA EL ORDENADOR. PROBLEMA EN BUCLE WHILE SUPONGO
        StringBuilder linea = new StringBuilder();
        String path ="multidescarga.log";
        File archivo = new File(path);
        FileReader fr = null;
        BufferedReader entrada = null;
        try {
            fr = new FileReader(archivo);
            entrada = new BufferedReader(fr);

            while(entrada.ready()){
                taLogger.appendText(linea + "\n");
                linea.append(entrada.readLine());
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void readerLog() {  TODO ESTE NO ME PINTA NI SIQUIERA NADA
        try {
            String text = "";
            String path ="multidescarga.log";
            File archivo = new File(path);
            List<String> linea = Files.readAllLines(archivo.toPath());
            for (String texto : linea) {
                text += texto;
                taLogger.setText(text);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
