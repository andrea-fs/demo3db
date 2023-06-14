package com.code.demo3db;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static javafx.application.Application.launch;
public class Main extends Application{
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Password.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 500);


        Image icon = new Image(getClass().getResourceAsStream("/icona.png"));
        stage.getIcons().add(icon);
        stage.setResizable(false);

        stage.setTitle("Autenticazione");
        stage.setScene(scene);
        stage.show();
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
}
