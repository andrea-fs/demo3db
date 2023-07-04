package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class Terapia implements Initializable {
    @FXML
    private DatePicker data;
    @FXML
    private Button inserisci;
    @FXML
    private ComboBox<String> farmaco;
    @FXML
    private Spinner<Integer> dose;
    @FXML
    private Spinner<Integer> acquisizioni;
    @FXML
    private Label nonInserito;
    @FXML
    private TableView<TerapiaClass> tabellaTerapie;
    @FXML
    private TableColumn<TerapiaClass, LocalDate> colDataFine;
    @FXML
    private TableColumn<TerapiaClass, String> colFarmaco;
    @FXML
    private TableColumn<TerapiaClass, Integer> colDose;
    @FXML
    private TableColumn<TerapiaClass, Integer> colAcquisizioni;
    @FXML
    private CheckBox mese;
    @FXML
    private CheckBox settimana;
    @FXML
    private Button ripopola;
    @FXML
    private Label selezionaCheckBox;
    @FXML
    private Button eliminaTerapia;
    @FXML
    private Button modificaTerapia;

    private String matricola_M;
    private String matricola_P;
    TherapyModel model;

    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;
        settimana.setSelected(true);
        popolaTabella();

    }
    @FXML
    public void popolaTabella(){
        if(mese.isSelected() || settimana.isSelected()){
            try {
                TherapyModel model = TherapyModel.getInstance();
                boolean isMeseSelected = mese.isSelected();
                List<TerapiaClass> terapie = model.getTerapieForTable(matricola_P, isMeseSelected);
                tabellaTerapie.getItems().clear();
                tabellaTerapie.getItems().addAll(terapie);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            selezionaCheckBox.setText("Seleziona un'opzione");
            selezionaCheckBox.setTextFill(Color.DARKRED);
        }
    }
    public void controlCheck(ActionEvent e){
        CheckBox selectedCheckBox = (CheckBox) e.getSource();

        if (selectedCheckBox == settimana && mese.isSelected()) {
            mese.setSelected(false);
        } else if (selectedCheckBox == mese && settimana.isSelected()) {
            settimana.setSelected(false);
        }
    }
    @FXML
    public void eliminaTerapia(ActionEvent event) {
        TerapiaClass terapiaSelezionata = tabellaTerapie.getSelectionModel().getSelectedItem();
        if (terapiaSelezionata != null) {
            try {
                TherapyModel model = TherapyModel.getInstance();
                model.eliminaTerapia(terapiaSelezionata, matricola_P);
                tabellaTerapie.getItems().remove(terapiaSelezionata);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void modificaTerapia(ActionEvent event) {
        TerapiaClass terapiaSelezionata = tabellaTerapie.getSelectionModel().getSelectedItem();
        nonInserito.setText("");
        String text = farmaco.getValue();
        if (data.getValue() != null && !text.isEmpty()) {
            LocalDate selectedDate = data.getValue();
            if (terapiaSelezionata != null) {
                try {
                    TherapyModel model = TherapyModel.getInstance();
                    model.eliminaTerapia(terapiaSelezionata, matricola_P);
                    tabellaTerapie.getItems().remove(terapiaSelezionata);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {

                try {
                    model = TherapyModel.getInstance();
                    List<TerapiaClass> terapie = model.getTerapieByMatricola(matricola_P);
                    int count = 0;
                    LocalDate yesterday = LocalDate.now().minusDays(1);

                    for (TerapiaClass terapia : terapie) {
                        if (terapia.getDataFine().isAfter(yesterday)) {
                            count++;
                        }
                    }
                    if(count >= 4) {
                        nonInserito.setText("Sono già presenti 4 terapie");
                    }
                    else{
                        model.insertData(matricola_M, matricola_P, data.getValue(), farmaco.getValue(), dose.getValue(), acquisizioni.getValue());
                        System.out.println("Data inserita con successo nel database.");
                    }
                    data.setValue(null); // Resetta il valore
                } catch (SQLException e) {
                    System.out.println("Errore durante l'inserimento della data nel database: " + e.getMessage());
                }
            } catch (DateTimeParseException e) {
                System.out.println("Errore di formattazione della data: " + e.getMessage());
            }
        }
        else{
            nonInserito.setText("Problema con l'inserimento, riprovare");
            nonInserito.setTextFill(Color.DARKRED);
        }
        popolaTabella();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nonInserito.setText("");
        try {
            model = TherapyModel.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tabellaTerapie.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        colFarmaco.setCellValueFactory(new PropertyValueFactory<>("farmaco"));
        colDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        colAcquisizioni.setCellValueFactory(new PropertyValueFactory<>("acquisizioni"));

        mese.setOnAction(this::controlCheck);
        settimana.setOnAction(this::controlCheck);
        selezionaCheckBox.setText("");


        SpinnerValueFactory<Integer> valueAcquisizioni
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        acquisizioni.setValueFactory(valueAcquisizioni);

        SpinnerValueFactory<Integer> valueDose
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 2000, 200, 5);
        dose.setValueFactory(valueDose);

        data.setDayCellFactory(getDayCellFactory());
        data.setDayCellFactory(getDayCellFactory());
        inserisci.setOnAction(this::insertData);
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
        nonInserito.setText("");
        String text = farmaco.getValue();
        if (data.getValue() != null && !text.isEmpty()) {
            LocalDate selectedDate = data.getValue();

            try {

                try {
                    model = TherapyModel.getInstance();
                    List<TerapiaClass> terapie = model.getTerapieByMatricola(matricola_P);
                    int count = 0;
                    LocalDate yesterday = LocalDate.now().minusDays(1);

                    for (TerapiaClass terapia : terapie) {
                        if (terapia.getDataFine().isAfter(yesterday)) {
                            count++;
                        }
                    }
                    if(count >= 4) {
                        nonInserito.setText("Sono già presenti 4 terapie");
                    }
                    else{
                        model.insertData(matricola_M, matricola_P, data.getValue(), farmaco.getValue(), dose.getValue(), acquisizioni.getValue());
                        System.out.println("Data inserita con successo nel database.");
                    }
                    data.setValue(null); // Resetta il valore
                } catch (SQLException e) {
                    System.out.println("Errore durante l'inserimento della data nel database: " + e.getMessage());
                }
            } catch (DateTimeParseException e) {
                System.out.println("Errore di formattazione della data: " + e.getMessage());
            }
        }
        else{
            nonInserito.setText("Problema con l'inserimento, riprovare");
            nonInserito.setTextFill(Color.DARKRED);
        }
        popolaTabella();
    }


}
