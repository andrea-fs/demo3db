package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    private ComboBox<String> farmaco;
    @FXML
    private TextField sintomi;
    @FXML
    private Label datanonvalida;

    @FXML
    private Label oranonvalida;
    DataModel model;
    private String matricola;


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
    public void initializeData(String nomeUtente){
        matricola = nomeUtente;
        System.out.println(matricola);
    }
    private StringConverter<LocalDate> createStringConverter () {
        return new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String s) {
                if (s != null && !s.isEmpty()) {
                    try {
                        if (s.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            return LocalDate.parse(s, dateFormatter);
                        } else {
                            return null;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Errore nella data " + e.getMessage());
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };
    }
    /*
    @FXML
    void insertData(ActionEvent event) {
        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);


        if (farmaco.getValue() == null){ //
            farmaco.setValue("Nessuno"); //
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
                                    model.insertData(matricola, data.getValue(), ora.getValue(), SBP.getValue(), DBP.getValue(), farmaco.getValue(), dose.getValue(), sintomi.getText());
                                    System.out.println("Dati inseriti con successo nel database." + dose.getValue());


                                    //farmaco.clear();
                                    sintomi.clear();

                                    Model orgmodel = Model.getInstance();
                                    String medico_associato = orgmodel.getMedicoAssociato(matricola);

                                    AlertModel alert = AlertModel.getInstance();

                                    if((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() > 89 && DBP.getValue() < 95)){
                                        alert.insertAlert(matricola, medico_associato, "Grado 1 borderline", SBP.getValue(), DBP.getValue(), data.getValue());
                                    }
                                    else if((SBP.getValue() > 149 && SBP.getValue() < 160) && (DBP.getValue() > 94 && DBP.getValue() < 100)){
                                        alert.insertAlert(matricola, medico_associato, "Grado 1 lieve", SBP.getValue(), DBP.getValue(), data.getValue());

                                    }
                                    else if((SBP.getValue() > 159 && SBP.getValue() < 180) && (DBP.getValue() > 99 && DBP.getValue() < 110)){
                                        alert.insertAlert(matricola, medico_associato, "Grado 2 moderata", SBP.getValue(), DBP.getValue(), data.getValue());

                                    }
                                    else if((SBP.getValue() > 179) && (DBP.getValue() > 109)){
                                        alert.insertAlert(matricola, medico_associato, "Grado 3 grave", SBP.getValue(), DBP.getValue(), data.getValue());

                                    }
                                    else if((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() < 90)){
                                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata borderline", SBP.getValue(), DBP.getValue(), data.getValue());

                                    }
                                    else if((SBP.getValue() > 149) && (DBP.getValue() < 90)) {
                                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata", SBP.getValue(), DBP.getValue(), data.getValue());

                                    }
                                    SBP.getValueFactory().setValue(120); // Ripristina il valore predefinito
                                    DBP.getValueFactory().setValue(80);
                                    ora.getValueFactory().setValue(12);
                                    dose.getValueFactory().setValue(200);
                                    data.setValue(null);


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

     */



    @FXML
    public void insertData(ActionEvent event) {
        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);

        if (farmaco.getValue() == null) {
            farmaco.setValue("Nessuno");
        }

        if (sintomi.getText().isEmpty()) {
            sintomi.setText("Nessuno");
        }

        if (data.getValue() == null) {
            datanonvalida.setTextFill(Color.RED);
            return;
        }

        LocalDate selectedDate = data.getValue();

        if (selectedDate.isEqual(LocalDate.now()) && ora.getValue().compareTo(LocalTime.now().getHour()) >= 0) {
            oranonvalida.setTextFill(Color.RED);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateText = selectedDate.format(formatter);

            if (selectedDate.isBefore(LocalDate.of(1950, 1, 1))) {
                datanonvalida.setTextFill(Color.RED);
                return;
            }

            model = DataModel.getInstance();
            boolean controllo = model.controlloinserimento(matricola, data.getValue(), ora.getValue());

            if (controllo) {
                try {
                    model.insertData(matricola, data.getValue(), ora.getValue(), SBP.getValue(), DBP.getValue(), farmaco.getValue(), dose.getValue(), sintomi.getText());
                    System.out.println("Dati inseriti con successo nel database." + dose.getValue());

                    sintomi.clear();

                    Model orgmodel = Model.getInstance();
                    String medico_associato = orgmodel.getMedicoAssociato(matricola);

                    AlertModel alert = AlertModel.getInstance();

                    if ((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() > 89 && DBP.getValue() < 95)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 1 borderline", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 149 && SBP.getValue() < 160) && (DBP.getValue() > 94 && DBP.getValue() < 100)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 1 lieve", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 159 && SBP.getValue() < 180) && (DBP.getValue() > 99 && DBP.getValue() < 110)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 2 moderata", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 179) && (DBP.getValue() > 109)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 3 grave", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() < 90)) {
                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata borderline", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 149) && (DBP.getValue() < 90)) {
                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata", SBP.getValue(), DBP.getValue(), data.getValue());
                    }

                    SBP.getValueFactory().setValue(120);
                    DBP.getValueFactory().setValue(80);
                    ora.getValueFactory().setValue(12);
                    dose.getValueFactory().setValue(200);
                    data.setValue(null);
                } catch (SQLException e) {
                    System.out.println("Errore durante l'inserimento dei dati nel database: " + e.getMessage());
                }
            } else {
                System.out.println("Inserimento non valido");
            }
        } catch (DateTimeParseException | SQLException e) {
            datanonvalida.setTextFill(Color.RED);
            System.out.println("Errore durante il parsing della data: " + e.getMessage());
        }
    }


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

        if(data.getConverter() == null){
            data.setConverter(createStringConverter());
        }
        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);
        ObservableList<String> farmaciOptions = FXCollections.observableArrayList(
                "Acebutololo",
                "Acido etacrinico",
                "Amlodipina",
                "Amiloride",
                "Azilsartan",
                "Benazepril",
                "Bendroflumetiazide",
                "Bumetanide",
                "Candesartan",
                "Captopril",
                "Carvedilolo",
                "Carvedilolo",
                "Clonidina",
                "Clonidina TTS (patch)",
                "Diltiazem, a rilascio prolungato",
                "Diltiazem, a rilascio prolungato",
                "Doxazosina",
                "Enalapril",
                "Eprosartan",
                "Eplerenone",
                "Felodipina",
                "Fosinopril",
                "Furosemide",
                "Guanabenz",
                "Guanfacina",
                "Idralazina",
                "Irbesartan",
                "Isradipina",
                "Labetalolo",
                "Lisinopril",
                "Labetalolo",
                "Losartan",
                "Metildopa",
                "Metoprololo",
                "Metoprololo (a rilascio prolungato)",
                "Minoxidil",
                "Nadololo",
                "Nebivololo",
                "Nicardipina",
                "Nicardipina, a rilascio prolungato",
                "Nifedipina, a lento rilascio",
                "Nisoldipina",
                "Olmesartan",
                "Pindololo",
                "Prazosina",
                "Perindopril erbumine",
                "Penbutololo",
                "Propranololo",
                "Propranololo, lunga durata d'azione",
                "Quinapril",
                "Ramipril",
                "Spironolattone",
                "Telmisartan",
                "Terazosina",
                "Timololo",
                "Torsemide",
                "Trandolapril",
                "Triamterene",
                "Valsartan",
                "Verapamil",
                "Verapamil, a rilascio prolungato"
        );
        farmaco.setItems(farmaciOptions);


    }

}
