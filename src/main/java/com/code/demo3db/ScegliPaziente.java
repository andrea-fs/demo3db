package com.code.demo3db;

import com.code.demo3db.Paziente;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ScegliPaziente implements Initializable {
    private MedicoInterfaccia medicoInterfacciaController;

    @FXML
    private TableView<Paziente> tabella;
    @FXML
    private TableColumn<Paziente, String> colonnaMatricola;
    @FXML
    private TableColumn<Paziente, String> colonnaNome;
    @FXML
    private TableColumn<Paziente, String> colonnaCognome;
    private String matricola;
    @FXML
    private Label selezionato;
    @FXML
    private Label scegliPazienteAdatto;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colonnaMatricola.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricola()));
        colonnaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colonnaCognome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCognome()));
        caricaPazienti();
        selezionato.setText("clicca nella tabella un paziente");
        scegliPazienteAdatto.setText("");
    }
    public void initializeData(String matricola) {
        this.matricola = matricola;
        caricaPazienti();
    }
    private void caricaPazienti() {

        try {
            Model model = Model.getInstance();
            List<Paziente> pazienti = model.getPazientiByMedico();
            tabella.getItems().addAll(pazienti);

            tabella.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    Paziente pazienteSelezionato = tabella.getSelectionModel().getSelectedItem();
                    if (pazienteSelezionato != null) {
                        String matricolaSelezionata = pazienteSelezionato.getMatricola();
                        medicoInterfacciaController.setMatricola_P(matricolaSelezionata);
                        selezionato.setText(matricolaSelezionata);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setMedicoInterfacciaController(MedicoInterfaccia controller) {
        this.medicoInterfacciaController = controller;
    }
    public void selezionarePazienteAdatto(){
        scegliPazienteAdatto.setText("Seleziona un paziente adatto");
        scegliPazienteAdatto.setTextFill(Color.DARKRED);
    }
}
