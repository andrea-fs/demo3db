package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    @FXML
    private LineChart<String, Integer> grafico;
    @FXML
    private Button caricaRiepilogo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabellaTerapie.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        colFarmaco.setCellValueFactory(new PropertyValueFactory<>("farmaco"));
        colDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        colAcquisizioni.setCellValueFactory(new PropertyValueFactory<>("acquisizioni"));
        mese.setOnAction(this::controlCheck);
        settimana.setOnAction(this::controlCheck);
        settimana.setSelected(true);
        initializeLineChart();

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
    @FXML
    private void initializeLineChart() {
        XYChart.Series<String, Integer> sbpSeries = new XYChart.Series<>();
        sbpSeries.setName("SBP");

        XYChart.Series<String, Integer> dbpSeries = new XYChart.Series<>();
        dbpSeries.setName("DBP");

        try {
            DataModel model = DataModel.getInstance();
            ObservableList<Measurement> measurements = model.getMeasurements(matricola_P, settimana.isSelected());

            for (Measurement measurement : measurements) {//è un for each che la v. mesurament prende il valore degli ogg mes. man mano che scorre
                LocalDate date = measurement.getDate();
                int sbp = measurement.getSbp();
                int dbp = measurement.getDbp();

                sbpSeries.getData().add(new XYChart.Data<>(date.toString(), sbp));
                dbpSeries.getData().add(new XYChart.Data<>(date.toString(), dbp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<XYChart.Series<String, Integer>> seriesList = FXCollections.observableArrayList();
        seriesList.addAll(sbpSeries, dbpSeries);
        grafico.setData(seriesList);
    }

}