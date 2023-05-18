package com.code.demo3db;


import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String nomeutenteinserito;

    private final String COLORE_STATICO = "-fx-background-color: #ebebeb";
    private final String COLORE_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #a2a2a2; -fx-font-size: 11px;";
    private final String COLORE_CLIK_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #c4c43d; -fx-font-size: 11px;";
    @FXML
    private TextField passwordInput;

    @FXML
    private TextField userInput;

    @FXML
    private Button buttonLogin = new Button();

    //private Image image_url = new Image(getClass().getResourceAsStream("/background_1.jpg"));

    private  Model model;

    public Controller() {
    }

    public String getNomeInserito() {
        return nomeutenteinserito;
    }

    public void controlloUtente(ActionEvent event) throws SQLException, IOException {
        model = Model.getInstance();
        boolean response = (model.controlloUtente(userInput.getText(),passwordInput.getText()));
        if (response){
            nomeutenteinserito = userInput.getText();
            passwordInput.clear();
            switchToSceneMedico(event);
        }
        else{
            passwordInput.clear();

        }
    }

    public void switchToSceneMedico(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userInput.setPromptText("Es. Mario Rossi");
        passwordInput.setPromptText("Password");
        buttonLogin.setStyle(COLORE_STATICO);

        buttonLogin.setOnMouseEntered(e-> buttonLogin.setStyle(COLORE_DINAMICO));
        buttonLogin.setOnMouseExited(e-> buttonLogin.setStyle(COLORE_STATICO));
        buttonLogin.setOnMouseClicked(e -> buttonLogin.setStyle(COLORE_CLIK_DINAMICO));


    }


}
