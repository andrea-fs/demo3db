package com.code.demo3db;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;


import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{   //TODO mettere vincoli su lunghezze x effetto grafico
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Password.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 500);


        Image icon = new Image(getClass().getResourceAsStream("/icona.png"));
        stage.getIcons().add(icon);
        stage.setResizable(false);

        stage.setTitle("Autenticazione");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            indietro(stage);
        });

    }
    public static void main(String[] args) throws SQLException {
        Model db = new Model();
        Connection conn = db.connessione();
        //db.createTable(conn,"employee");
        //db.insert_raw("archivio","M1","password","Andre","Foss","M");
        //db.insert_raw("archivio","P1","ciao","Fra","Si","P");
        //db.insert_raw("archivio","P2","bo","Gio","Se","P");
        launch();
    }
    public void indietro (Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Stai per chiudere l'applicazione\nTutti i dati non salvati andranno persi!");
        alert.setContentText("Termino applicazione?");

        if (alert.showAndWait().get() == ButtonType.OK) {

            stage.close();
            Platform.exit();
        }
    }
}
