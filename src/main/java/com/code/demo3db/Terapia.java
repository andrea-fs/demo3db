package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class Terapia implements Initializable {
    @FXML
    private DatePicker data;
    @FXML
    private Button inserisci;
    @FXML
    private TextField farmaco;
    @FXML
    private Spinner<Integer> dose;
    @FXML
    private Spinner<Integer> acquisizioni;
    private String matricola_M;
    private String matricola_P;
    TherapyModel model;

    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            model = TherapyModel.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SpinnerValueFactory<Integer> valueAcquisizioni
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        acquisizioni.setValueFactory(valueAcquisizioni);

        SpinnerValueFactory<Integer> valueDose
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 2000, 200, 5);
        dose.setValueFactory(valueDose);

        data.setDayCellFactory(getDayCellFactory());
        data.setDayCellFactory(getDayCellFactory());
        inserisci.setOnAction(this::insertData);

    }
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Disabilita le date passate
                setDisable(item.isBefore(LocalDate.now()));
            }
        };
    }

    @FXML
    void insertData(ActionEvent event) {
        if (data.getValue() != null) {
            LocalDate selectedDate = data.getValue();

            try {

                try {
                    model = TherapyModel.getInstance();
                    model.insertData(matricola_M, matricola_P, data.getValue(), farmaco.getText(), dose.getValue(), acquisizioni.getValue());
                    System.out.println("Data inserita con successo nel database.");

                    data.setValue(null); // Resetta il valore

                } catch (SQLException e) {
                    System.out.println("Errore durante l'inserimento della data nel database: " + e.getMessage());
                }
            } catch (DateTimeParseException e) {
                System.out.println("Errore di formattazione della data: " + e.getMessage());
            }
        }
        farmaco.clear();

    }


}
