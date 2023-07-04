package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
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
    @FXML
    private Label inserito;
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
        if (data.getValue() == null){
            return null;
        }
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
        inserito.setText("");
        datanonvalida.setTextFill(Color.WHITE);
        oranonvalida.setTextFill(Color.WHITE);
        boolean presenzasintomi = true;

        if (farmaco.getValue() == null) {
            farmaco.setValue("Nessuno");
        }

        if (sintomi.getText().isEmpty()) {
            presenzasintomi = false;
            sintomi.setText("Nessuno");
        }

        if (data.getValue() == null) {
            System.out.println("data nulla");
            datanonvalida.setTextFill(Color.RED);
            return;
        }
        if (data.getValue().isBefore(LocalDate.of(1950, 1, 1))) {
            datanonvalida.setTextFill(Color.RED);
            return;
        }

        LocalDate selectedDate = data.getValue();

        if (selectedDate.isEqual(LocalDate.now()) && ora.getValue().compareTo(LocalTime.now().getHour()) > 0) {
            oranonvalida.setTextFill(Color.RED);
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate datecorrect = data.getValue();
        String inputCorrect = datecorrect.format(formatter);
        try {
            LocalDate parsedDate = LocalDate.parse(inputCorrect, formatter);
            //String dateText = selectedDate.format(formatter);

            model = DataModel.getInstance();
            boolean controllo = model.controlloinserimento(matricola, parsedDate, ora.getValue(), farmaco.getValue());

            if (controllo) {

                //inserisce
                try {
                    model.insertData(matricola, parsedDate, ora.getValue(), SBP.getValue(), DBP.getValue(), farmaco.getValue(), dose.getValue(), sintomi.getText());
                    System.out.println("Dati inseriti con successo nel database." + dose.getValue());
                    inserito.setText("inserito");
                    inserito.setStyle("-fx-background-color:  #689f38;");
                    sintomi.clear();

                    Model orgmodel = Model.getInstance();
                    String medico_associato = orgmodel.getMedicoAssociato(matricola);

                    AlertModel alert = AlertModel.getInstance();

                    if ((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() > 89 && DBP.getValue() < 95)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 1 borderline", SBP.getValue(), DBP.getValue(), parsedDate);
                    } else if ((SBP.getValue() > 149 && SBP.getValue() < 160) && (DBP.getValue() > 94 && DBP.getValue() < 100)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 1 lieve", SBP.getValue(), DBP.getValue(), parsedDate);
                    } else if ((SBP.getValue() > 159 && SBP.getValue() < 180) && (DBP.getValue() > 99 && DBP.getValue() < 110)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 2 moderata", SBP.getValue(), DBP.getValue(), parsedDate);
                    } else if ((SBP.getValue() > 179) && (DBP.getValue() > 109)) {
                        alert.insertAlert(matricola, medico_associato, "Grado 3 grave", SBP.getValue(), DBP.getValue(), data.getValue());
                    } else if ((SBP.getValue() > 139 && SBP.getValue() < 150) && (DBP.getValue() < 90)) {
                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata borderline", SBP.getValue(), DBP.getValue(), parsedDate);
                    } else if ((SBP.getValue() > 149) && (DBP.getValue() < 90)) {
                        alert.insertAlert(matricola, medico_associato, "Sistolica isolata", SBP.getValue(), DBP.getValue(), parsedDate);
                    }


                    TherapyModel therapy = TherapyModel.getInstance();

                    if (farmaco.getValue() != "Nessuno") {
                        List<Pair<String, Integer>> terapieList = therapy.getFarmaciTerapieAttive(matricola);
                        boolean isFarmacoPresente = false;
                        boolean isDoseCorretta = false;

                        for (Pair<String, Integer> terapia : terapieList) {
                            String terapiaFarmaco = terapia.getKey();
                            int terapiaDose = terapia.getValue();
                            //System.out.println("                               "+terapiaFarmaco + farmaco.getValue());

                            if (terapiaFarmaco.equals(farmaco.getValue())) {
                                isFarmacoPresente = true;

                                if (terapiaDose == dose.getValue()) {
                                    isDoseCorretta = true;
                                }
                            }
                        }

                        if (!isFarmacoPresente) {
                            alert.insertAlert(matricola, medico_associato, "Farmaco esterno a terapie", SBP.getValue(), DBP.getValue(), parsedDate);
                        } else if (!isDoseCorretta) {
                            alert.insertAlert(matricola, medico_associato, "Dose errata", SBP.getValue(), DBP.getValue(), parsedDate);
                        }
                    }

                    if(presenzasintomi){
                        alert.insertAlert(matricola, medico_associato, "Sintomi o malattie", SBP.getValue(), DBP.getValue(), parsedDate);
                    }


                    SBP.getValueFactory().setValue(0);
                    DBP.getValueFactory().setValue(0);
                    ora.getValueFactory().setValue(12);
                    dose.getValueFactory().setValue(200);
                    data.setValue(null);
                } catch (SQLException e) {
                    System.out.println("Errore durante l'inserimento dei dati nel database: ");
                }
            } else {
                System.out.println("Inserimento non valido");
            }
        } catch (DateTimeParseException | SQLException e) {
            datanonvalida.setTextFill(Color.RED);
            System.out.println("Errore durante il parsing della data: ");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            model = DataModel.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        inserito.setText("");
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
