package com.sanvalero.multidescarga;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Creado por @ author: Pedro Orós
 * el 22/11/2020
 */
public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;

    private static final Logger logger = LogManager.getLogger(DownloaderController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada");
        updateMessage("Conectando con el servidor. . .");

        URLConnection urlConnection = url.openConnection();
        double fileSize = urlConnection.getContentLength();
        double fileSizeInMegas = fileSize / (Math.pow(1024, 2));
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;
        double downloadProgressMegas = 0;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);
            downloadProgressMegas = ((double) totalRead / fileSizeInMegas);

            updateProgress(downloadProgressMegas, fileSizeInMegas);

            updateProgress(downloadProgress, 1);
            updateMessage(Math.round(downloadProgress * 100) + " %" + "_____________________" + (int) downloadProgressMegas / 10000 + " Mb    de    " + (int) fileSizeInMegas + " Mb");

            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalRead += bytesRead;
            if(isCancelled()) {
                logger.trace("Descarga " + url.toString() + " cancelada");
                return null;
            }
        }

        updateProgress(1, 1);
        updateMessage("100 %" + "______de_____" + "Total megas descargados: " + (int) fileSizeInMegas + "Mb");

        logger.trace("Descarga " + url.toString() + " finalizada");
        return null;
    }
}
