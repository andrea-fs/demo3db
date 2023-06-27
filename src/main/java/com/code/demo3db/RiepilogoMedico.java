package com.code.demo3db;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RiepilogoMedico implements Initializable {

    private String matricola_M;
    private String matricola_P;
    @FXML
    private TextField fattoriDiRischio;
    @FXML
    private TextField coomorbidita;
    @FXML
    private TextField patologie;
    @FXML
    private TextField altro;
    @FXML
    private Button insert;
    DatiPazienteModel model;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;
    }
    public void inserRaw(){
        try {
            model = DatiPazienteModel.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        model.insert_raw(matricola_P,fattoriDiRischio.getText(),patologie.getText(),coomorbidita.getText(),altro.getText(),matricola_M, LocalDate.now());
    }
}