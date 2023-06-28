package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RiepilogoMedico implements Initializable {

    private String matricola_M;
    private String matricola_P;

    @FXML
    private CheckBox mese;
    @FXML
    private CheckBox settimana;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabellaTerapie.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        colFarmaco.setCellValueFactory(new PropertyValueFactory<>("farmaco"));
        colDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        colAcquisizioni.setCellValueFactory(new PropertyValueFactory<>("acquisizioni"));
        mese.setOnAction(this::controlCheck);
        settimana.setOnAction(this::controlCheck);
    }

    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;

        try {
            TherapyModel model = TherapyModel.getInstance();
            boolean isMeseSelected = mese.isSelected();
            List<TerapiaClass> terapie = model.getTerapieForTable(matricola_P, isMeseSelected);
            tabellaTerapie.getItems().addAll(terapie);
        } catch (SQLException e) {
            e.printStackTrace();
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


}