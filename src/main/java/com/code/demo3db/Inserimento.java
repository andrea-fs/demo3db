package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.function.Predicate;

import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Inserimento implements Initializable {
    //TODO controllare inserimenti già fatti da databse
    @FXML
    private Spinner<Integer> SBP;
    @FXML
    private Spinner<Integer> DBP;
    @FXML
    private Spinner<Integer> ora;
    @FXML
    private Spinner<Integer> dose;
    @FXML
    private DatePicker data;
    @FXML
    private Button inserisci;
    @FXML
    private TextField farmaco;
    @FXML
    private TextField sintomi;
    @FXML
    private Label datanonvalida;

    @FXML
    private Label oranonvalida;
    DataModel model;
    private String matricola;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            model = DataModel.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //int orattuale = LocalTime.now().getHour();
        SpinnerValueFactory<Integer> valueSBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 0);
        SBP.setValueFactory(valueSBP);
        SpinnerValueFactory<Integer> valueDBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 130, 0);
        DBP.setValueFactory(valueDBP);
        SpinnerValueFactory<Integer> valueOra
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        ora.setValueFactory(valueOra);
        SpinnerValueFactory<Integer> valueDose
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 2000, 200, 5);
        dose.setValueFactory(valueDose);

        data.setDayCellFactory(getDayCellFactory());
        data.setDayCellFactory(getDayCellFactory());
        inserisci.setOnAction(this::insertData);

        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);
    }
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Disabilita le date future
                setDisable(item.isAfter(LocalDate.now()));
            }
        };
    }
    // TODO LA DATA PUò ESSERE INSERITO DI TUTTO E DARE ERRORE
    public void initializeData(String nomeUtente){
        matricola = nomeUtente;
        System.out.println(matricola);
    }
    @FXML
    void insertData(ActionEvent event) {
        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);
        if (farmaco.getText().isEmpty()){
            farmaco.setText("Nessuno");
        }
        if (sintomi.getText().isEmpty()){
            sintomi.setText("Nessuno");
        }// TODO lettere nella data controlla


        if(data.getValue() == null){
            datanonvalida.setTextFill(Color.RED);
        }
        else {
            LocalDate selectedDate = data.getValue();
            System.out.println(selectedDate.isEqual(LocalDate.now()));
            System.out.println(ora.getValue().compareTo(LocalTime.now().getHour()) >= 0);
            System.out.println(LocalTime.now().getHour());
            System.out.println(ora.getValue());

            // controllo che non sia futura l'ora
            if (selectedDate.isEqual(LocalDate.now()) && ora.getValue() > LocalTime.now().getHour()) {
                oranonvalida.setTextFill(Color.RED);
            } else {

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String dateText = selectedDate.format(formatter);


                    // La data è valida
                    if (selectedDate.isBefore(LocalDate.of(1950, 1, 1))) {
                        datanonvalida.setTextFill(Color.RED);
                    }

                    // la data non è prima del 1950
                    else {
                        try {
                            model = DataModel.getInstance();
                            boolean controllo = model.controlloinserimento(matricola, data.getValue(), ora.getValue());
                            if (controllo) {
                                try {
                                    model.insertData(matricola, data.getValue(), ora.getValue(), SBP.getValue(), DBP.getValue(), farmaco.getText(), dose.getValue(), sintomi.getText());
                                    System.out.println("Dati inseriti con successo nel database." + dose.getValue());

                                    SBP.getValueFactory().setValue(120); // Ripristina il valore predefinito
                                    DBP.getValueFactory().setValue(80);
                                    ora.getValueFactory().setValue(12);
                                    dose.getValueFactory().setValue(200);
                                    data.setValue(null);
                                    farmaco.clear();
                                    sintomi.clear();


                                } catch (SQLException e) {
                                    System.out.println("Errore durante l'inserimento dei dati nel database: " + e.getMessage());
                                }
                            } else {
                                System.out.println("inserimento non valido");
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());

                        }
                    }
                } catch (DateTimeParseException e) {
                    datanonvalida.setTextFill(Color.RED);
                    System.out.println("errore nel try di prase");
                    return;
                } catch (Exception e) {
                    datanonvalida.setTextFill(Color.RED);
                    System.out.println("errore nel try di prase");
                    return;
                }
            }
        }
    }
}
