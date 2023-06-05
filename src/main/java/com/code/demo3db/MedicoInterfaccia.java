package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MedicoInterfaccia implements Initializable {

    private final String COLORE_STATICO = "-fx-background-color: #b2b2b2";
    private final String COLORE_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #a2a2a2; -fx-font-size: 11px;";
    private final String COLORE_CLIK_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #c4c43d; -fx-font-size: 11px;";
    @FXML
    private Button indietro = new Button();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label nomeUtenteLabel;

    private String nomeUtente;

    @FXML
    public void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Password.fxml"));

        stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        indietro.setStyle(COLORE_STATICO);
        indietro.setOnMouseEntered(e-> indietro.setStyle(COLORE_DINAMICO));
        indietro.setOnMouseExited(e-> indietro.setStyle(COLORE_STATICO));
        indietro.setOnMouseClicked(e -> indietro.setStyle(COLORE_CLIK_DINAMICO));
        System.out.println(nomeUtenteLabel);
    }

    public void initializeData(String nomeUtente) {

        // TODO
        this.nomeUtente = nomeUtente;
        nomeUtenteLabel.setText(nomeUtente);
    }
}
