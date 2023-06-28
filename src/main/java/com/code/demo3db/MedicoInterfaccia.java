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


public class MedicoInterfaccia implements Initializable {

    private final String COLORE_STATICO = "-fx-background-color: #d3ccff";
    private final String COLORE_DINAMICO = "-fx-background-color: #7980d3";
    private final String DASHBOARD_STATICO = "-fx-background-color: #d3ccff";
    private final String DASHBOARD_DINAMICO = "-fx-background-color:  #7980d3; -fx-font-weight: bold;";
    @FXML
    private Button indietro = new Button();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label nomeUtenteLabel;

    private String nomeUtente;
    @FXML
    private StackPane contentArea;
    @FXML
    private Button buttonTerapia;
    @FXML
    private Button buttonPaziente;
    @FXML
    private Button buttonRiepilogo;
    @FXML
    private Button buttonFattori;
    @FXML
    private Label nome;
    @FXML
    private Label cognome;
    private  Model model;
    private String matricola_P = "";



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        indietro.setStyle(COLORE_STATICO);
        indietro.setOnMouseEntered(e-> indietro.setStyle(COLORE_DINAMICO));
        indietro.setOnMouseExited(e-> indietro.setStyle(COLORE_STATICO));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScegliPaziente.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            ScegliPaziente scegli = loader.getController();
            scegli.setMedicoInterfacciaController(this);
            scegli.initializeData(nomeUtente);
            System.out.println("vvvvv" + nomeUtente);

        } catch (IOException e){
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
        indietro.setOnMouseEntered(e-> indietro.setStyle(COLORE_DINAMICO));
        indietro.setOnMouseExited(e-> indietro.setStyle(COLORE_STATICO));

        buttonPaziente.setOnMouseEntered(e-> buttonPaziente.setStyle(DASHBOARD_DINAMICO));
        buttonPaziente.setOnMouseExited(e-> buttonPaziente.setStyle(DASHBOARD_STATICO));
        buttonTerapia.setOnMouseEntered(e-> buttonTerapia.setStyle(DASHBOARD_DINAMICO));
        buttonTerapia.setOnMouseExited(e-> buttonTerapia.setStyle(DASHBOARD_STATICO));
        buttonRiepilogo.setOnMouseEntered(e-> buttonRiepilogo.setStyle(DASHBOARD_DINAMICO));
        buttonRiepilogo.setOnMouseExited(e-> buttonRiepilogo.setStyle(DASHBOARD_STATICO));
        buttonFattori.setOnMouseEntered(e-> buttonFattori.setStyle(DASHBOARD_DINAMICO));
        buttonFattori.setOnMouseExited(e-> buttonFattori.setStyle(DASHBOARD_STATICO));

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
    public void pazeinte(javafx.event.ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScegliPaziente.fxml"));
        Parent fxml = loader.load();
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
        ScegliPaziente scegli = loader.getController();
        scegli.setMedicoInterfacciaController(this);
        scegli.initializeData(nomeUtente);
        System.out.println("vvvvv" + nomeUtente);
    }
    public void terapia(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        String medico_associato;
        Model model = Model.getInstance();
        medico_associato = model.getMedicoAssociato(matricola_P);

        if(!matricola_P.isEmpty() && medico_associato.equals(this.nomeUtente)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Terapia.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            Terapia therapy = loader.getController();
            therapy.initializeData(nomeUtente, matricola_P);
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScegliPaziente.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            ScegliPaziente scegli = loader.getController();
            scegli.setMedicoInterfacciaController(this);
            scegli.initializeData(nomeUtente);
            System.out.println("vvvvv" + nomeUtente);
            scegli.selezionarePazienteAdatto();
        }
    }
    public void riepilpgo(javafx.event.ActionEvent actionEvent) throws IOException{
        if(!matricola_P.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RiepilogoMedico.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            RiepilogoMedico riepilogoMedico = loader.getController();
            riepilogoMedico.initializeData(nomeUtente, matricola_P);
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScegliPaziente.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            ScegliPaziente scegli = loader.getController();
            scegli.setMedicoInterfacciaController(this);
            scegli.initializeData(nomeUtente);
            System.out.println("vvvvv" + nomeUtente);
            scegli.selezionarePazienteAdatto();
        }
    }
    public void fattori(javafx.event.ActionEvent actionEvent) throws IOException{
        if(!matricola_P.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FattoriDiRischio.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            FattoriDiRischio fattoriDiRischio = loader.getController();
            fattoriDiRischio.initializeData(nomeUtente, matricola_P);
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScegliPaziente.fxml"));
            Parent fxml = loader.load();
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
            ScegliPaziente scegli = loader.getController();
            scegli.setMedicoInterfacciaController(this);
            scegli.initializeData(nomeUtente);
            System.out.println("vvvvv" + nomeUtente);
            scegli.selezionarePazienteAdatto();
        }
    }
    @FXML
    public void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Password.fxml"));

        stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setMatricola_P(String matricolaSelezionata) {
        matricola_P = matricolaSelezionata;
        System.out.println(matricola_P);
    }
}
