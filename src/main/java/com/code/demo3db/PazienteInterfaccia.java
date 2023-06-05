package com.code.demo3db;

import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PazienteInterfaccia implements Initializable{


    private final String COLORE_STATICO = "-fx-background-color: #b2b2b2";
    private final String COLORE_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #a2a2a2; -fx-font-size: 11px;";
    private final String COLORE_CLIK_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #c4c43d; -fx-font-size: 11px;";
    @FXML
    private Button indietro = new Button();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button InserimentoDati;
    @FXML
    private Label nomeUtenteLabel;
    @FXML
    private Rectangle rettangolo;
    private String nomeUtente;
    @FXML
    private AnchorPane mainPane;

    @FXML
    public void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Password.fxml"));

        stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToInserimento(ActionEvent event) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InserimentoPaziente.fxml"));
        Parent root = loader.load();
        InserimentoPaziente inserimentoPaziente = loader.getController();
        inserimentoPaziente.initializeData(nomeUtenteLabel.getText());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPane.setMinSize(500, 200);

        indietro.setOnMouseEntered(e-> indietro.setStyle(COLORE_DINAMICO));
        indietro.setOnMouseExited(e-> indietro.setStyle(COLORE_STATICO));
        indietro.setOnMouseClicked(e -> indietro.setStyle(COLORE_CLIK_DINAMICO));
        System.out.println(nomeUtenteLabel);
        DoubleBinding rettangoloWidth = mainPane.widthProperty().divide(5);
        rettangolo.widthProperty().bind(rettangoloWidth);
        rettangolo.heightProperty().bind(mainPane.heightProperty());

       // mainPane.getChildren().add(rettangolo);

    }
    public void initializeData(String nomeUtente) {

        // TODO
        this.nomeUtente = nomeUtente;
        nomeUtenteLabel.setText(nomeUtente);


    }

}
