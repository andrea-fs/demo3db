package com.code.demo3db;

import com.code.demo3db.Paziente;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ScegliPaziente implements Initializable {
    @FXML
    private TableView<Paziente> tabella;
    @FXML
    private TableColumn<Paziente, String> colonnaMatricola;
    @FXML
    private TableColumn<Paziente, String> colonnaNome;
    @FXML
    private TableColumn<Paziente, String> colonnaCognome;    private String matricola;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colonnaMatricola.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricola()));
        colonnaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colonnaCognome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCognome()));
        caricaPazienti();
    }
    public void initializeData(String matricola) {
        this.matricola = matricola;
        caricaPazienti();
    }
    private void caricaPazienti() {

        try {
            Model model = Model.getInstance();
            List<Paziente> pazienti = model.getPazientiByMedico(matricola);
            tabella.getItems().addAll(pazienti);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
