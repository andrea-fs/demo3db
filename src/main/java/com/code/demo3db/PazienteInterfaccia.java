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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PazienteInterfaccia implements Initializable{


    private final String COLORE_STATICO = "-fx-background-color: #ffe57f";
    private final String COLORE_DINAMICO = "-fx-background-color: #ffd600";
    private final String DASHBOARD_STATICO = "-fx-background-color: #ffe57f";
    private final String DASHBOARD_DINAMICO = "-fx-background-color:  #ffd600; -fx-font-weight: bold;";

    @FXML
    private Button indietro = new Button();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button InserimentoDati;
    @FXML
    private Label nomeUtenteLabel;
    private String nomeUtente;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label nome;
    @FXML
    private Label cognome;
    private  Model model;
    @FXML
    private StackPane contentArea;
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonInserimento;
    @FXML
    private Button buttonRiepilogo;
    @FXML
    private Button buttonMedico;




    /*public void switchToInserimento(ActionEvent event) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("MedicoInterfaccia.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InserimentoPaziente.fxml"));
        Parent root = loader.load();
        InserimentoPaziente inserimentoPaziente = loader.getController();

        inserimentoPaziente.initializeData(nomeUtenteLabel.getText(), nome.getText(), cognome.getText());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //mainPane.setMinSize(500, 200);

        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("Home.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
        } catch (IOException e){
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }

        indietro.setOnMouseEntered(e-> indietro.setStyle(COLORE_DINAMICO));
        indietro.setOnMouseExited(e-> indietro.setStyle(COLORE_STATICO));

        buttonHome.setOnMouseEntered(e-> buttonHome.setStyle(DASHBOARD_DINAMICO));
        buttonHome.setOnMouseExited(e-> buttonHome.setStyle(DASHBOARD_STATICO));
        buttonInserimento.setOnMouseEntered(e-> buttonInserimento.setStyle(DASHBOARD_DINAMICO));
        buttonInserimento.setOnMouseExited(e-> buttonInserimento.setStyle(DASHBOARD_STATICO));
        buttonRiepilogo.setOnMouseEntered(e-> buttonRiepilogo.setStyle(DASHBOARD_DINAMICO));
        buttonRiepilogo.setOnMouseExited(e-> buttonRiepilogo.setStyle(DASHBOARD_STATICO));
        buttonMedico.setOnMouseEntered(e-> buttonMedico.setStyle(DASHBOARD_DINAMICO));
        buttonMedico.setOnMouseExited(e-> buttonMedico.setStyle(DASHBOARD_STATICO));

    }
    public void initializeData(String nomeUtente) {



        // TODO
        this.nomeUtente = nomeUtente;
        nomeUtenteLabel.setText(nomeUtente);

        try {
            model = Model.getInstance();
            String[] nomeCognome = model.getDatafromMatricola("archivio", nomeUtente);
            if (nomeCognome != null) {
                nome.setText(nomeCognome[0]);
                cognome.setText(nomeCognome[1]);

            } else {
                System.out.println("utente non trovato");
                nome.setText("");
                cognome.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            nome.setText("");
            cognome.setText("");
        }
    }

    public void home(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("Home.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
    public void inserimento(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("Inserimento.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
    public void contatti(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("Contatti.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
    public void riepilpgo(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("Riepilogo.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }
    @FXML
    public void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Password.fxml"));


        stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
