package com.code.demo3db;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static javafx.application.Application.launch;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;

public class Admin implements Initializable {

    @FXML
    private TableView<ArchivioRow> tabella;
    private Model model;
    private Stage stage;
    private Scene scene;
    @FXML
    private Button insert_button;
    @FXML
    private Button indietro;
    @FXML
    private Button remove_button;
    @FXML
    private TextField matricola;
    @FXML
    private TextField password;
    @FXML
    private TextField nome;
    @FXML
    private TextField cognome;
    @FXML
    private TextField medicoAssociato;
    @FXML
    private ChoiceBox<String> MedicoPaziente;

    private String tablename = "archivio";
    // TODO IMPORTANTE CONTROLLA INSERIMENTO MINIMO PASSEORD ECC. !!!!!!!!!!
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            model = Model.getInstance();
            tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tabella.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("matricola"));
            tabella.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("password"));
            tabella.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("nome"));
            tabella.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("cognome"));
            tabella.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("medicoPaziente"));
            tabella.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("MedicoAssociato"));


            MedicoPaziente.getItems().addAll("M", "P");
            MedicoPaziente.setValue("M");

            tabella.getColumns();
            Bindings.bindContent(tabella.getItems(), model.getArchivioRows());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert_tupla(ActionEvent event) throws SQLException {
        String matricolaText = matricola.getText();
        String passwordText = password.getText();
        String nomeText = nome.getText();
        String cognomeText = cognome.getText();
        String medicoPazienteValue = MedicoPaziente.getValue();
        String medico_associato = null;
        if(medicoPazienteValue.equals("P")){
            medico_associato = medicoAssociato.getText();
        }

        ArchivioRow row = new ArchivioRow(matricolaText, passwordText, nomeText, cognomeText, medicoPazienteValue, medico_associato);
        model.insert_raw(tablename,matricolaText,passwordText,nomeText,cognomeText, medicoPazienteValue, medico_associato);
        //tabella.getItems().add(row);

        matricola.clear();
        password.clear();
        nome.clear();
        cognome.clear();
        if(!medicoAssociato.equals(null)) {
            medicoAssociato.clear();
        }
        MedicoPaziente.setValue("M");

    }

    public void remove_tupla(ActionEvent event) throws SQLException {
        ArchivioRow selectedRow = tabella.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            String matricola = selectedRow.getMatricola();
            // Esegui l'operazione di rimozione utilizzando il modello
            model.remove_raw("archivio", matricola);
            // Rimuovi la riga dalla tabella
            tabella.getItems().remove(selectedRow);
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
}
