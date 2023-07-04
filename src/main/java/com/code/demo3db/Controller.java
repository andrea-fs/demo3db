package com.code.demo3db;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String nomeutenteinserito;

    private final String COLORE_ERRORE = "-fx-text-fill: #972525";
    //private final String COLORE_STATICO = "-fx-background-color: #ebebeb";
    //private final String COLORE_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #a2a2a2; -fx-font-size: 11px;";
    //private final String COLORE_CLIK_DINAMICO = "-fx-background-color: #972525; -fx-text-fill: #c4c43d; -fx-font-size: 11px;";
    @FXML
    private Pane mainPane;
    @FXML
    private Pane anchorPane;
    @FXML
    private TextField passwordInput;
    @FXML
    private Label benvenuto;
    @FXML
    private Label error_password;
    @FXML
    private Label select_check;
    @FXML
    private Rectangle rettangolo;

    @FXML
    private TextField userInput;

    @FXML
    private Button buttonLogin = new Button();
    @FXML
    private CheckBox checkBoxMedico = new CheckBox();
    @FXML
    private CheckBox checkBoxPaziente = new CheckBox();

    //private Image image_url = new Image(getClass().getResourceAsStream("/background_1.jpg"));

    private  Model model;

    public Controller() {
    }
    public void controlloUtente(ActionEvent event) throws SQLException, IOException {
        error_password.setText("");
        select_check.setText("");
        if(userInput.getText().equals("Admin") && passwordInput.getText().equals("passwordAdmin")){ //TODO password sul codice: debole
            switchToSceneAdmin(event);
        }
        else {
            if (checkBoxPaziente.isSelected() || checkBoxMedico.isSelected()) {
                model = Model.getInstance();
                boolean response = (model.controlloUtente(userInput.getText(), passwordInput.getText()));
                if (response) {
                    nomeutenteinserito = userInput.getText();
                    passwordInput.clear();
                    String userType = model.getType(nomeutenteinserito);
                    if (userType != null) {
                        if (checkBoxMedico.isSelected() && userType.equals("M")) {
                            switchToSceneMedico(event);
                        } else if (checkBoxPaziente.isSelected() && userType.equals("P")) {
                            switchToScenePaziente(event);
                        } else {
                            passwordInput.clear();
                            rettangolo.setFill(Color.RED);
                            error_password.setText("password errata");
                        }

                    } else {
                        select_check.setText("Tipo di utente non valido");
                    }
                } else {
                    passwordInput.clear();
                    rettangolo.setFill(Color.RED);
                    error_password.setText("password errata");
                }
            } else {
                select_check.setText("Selezionare un portale");
                checkBoxMedico.setStyle("-fx-text-fill: red;");
                checkBoxPaziente.setStyle("-fx-text-fill: red;");

            }
        }
    }

    public void switchToSceneMedico(ActionEvent event) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MedicoInterfaccia.fxml"));
        Parent root = loader.load();
        MedicoInterfaccia medicoInterfaccia = loader.getController();
        medicoInterfaccia.initializeData(userInput.getText());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //stage.setResizable(true);
    }

    public void switchToScenePaziente(ActionEvent event) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PazienteInterfaccia.fxml"));
        Parent root = loader.load();
        PazienteInterfaccia pazienteInterfaccia = loader.getController();
        pazienteInterfaccia.initializeData(userInput.getText());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

    }
    public void switchToSceneAdmin(ActionEvent event) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);
    }

    public void controlCheck(ActionEvent e){
        CheckBox selectedCheckBox = (CheckBox) e.getSource();

        if (selectedCheckBox == checkBoxPaziente && checkBoxMedico.isSelected()) {
            checkBoxMedico.setSelected(false);
        } else if (selectedCheckBox == checkBoxMedico && checkBoxPaziente.isSelected()) {
            checkBoxPaziente.setSelected(false);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userInput.setPromptText("Matricola");
        passwordInput.setPromptText("Password");
        //buttonLogin.setStyle(COLORE_STATICO);
        error_password.setText("");
        error_password.setStyle(COLORE_ERRORE);
        select_check.setText("");
        select_check.setStyle(COLORE_ERRORE);
        buttonLogin.setCursor(Cursor.HAND);
        checkBoxMedico.setCursor(Cursor.HAND);
        checkBoxPaziente.setCursor(Cursor.HAND);


        checkBoxPaziente.setOnMouseClicked(e -> {
            checkBoxPaziente.setStyle("-fx-text-fill: white;");
            checkBoxMedico.setStyle("-fx-text-fill: white;");
        });

        checkBoxMedico.setOnMouseClicked(e -> {
            checkBoxMedico.setStyle("-fx-text-fill: white;");
            checkBoxPaziente.setStyle("-fx-text-fill: white;");
        });

        //buttonLogin.setOnMouseEntered(e-> buttonLogin.setStyle(COLORE_DINAMICO));
        //buttonLogin.setOnMouseExited(e-> buttonLogin.setStyle(COLORE_STATICO));
        //buttonLogin.setOnMouseClicked(e -> buttonLogin.setStyle(COLORE_CLIK_DINAMICO));
        checkBoxMedico.setOnAction(this::controlCheck);
        checkBoxPaziente.setOnAction(this::controlCheck);
//TODO enter
    }
}
