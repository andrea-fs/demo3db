package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RiepilogoMedico implements Initializable {

    private String matricola_M;
    private String matricola_P;
    @FXML
    private TableView tabellaAcquisizioni;
    @FXML
    private CheckBox mese;
    @FXML
    private CheckBox settimana;
    @FXML
    private LineChart<String, Integer> grafico;
    @FXML
    private Button caricaRiepilogo;
    @FXML
    private TableColumn<AcquisizioniClass, String> matricolaColumn;
    @FXML
    private TableColumn<AcquisizioniClass, LocalDate> dataColumn;
    @FXML
    private TableColumn<AcquisizioniClass, Integer> oraColumn;
    @FXML
    private TableColumn<AcquisizioniClass, Integer> sbpColumn;
    @FXML
    private TableColumn<AcquisizioniClass, Integer> dbpColumn;
    @FXML
    private TableColumn<AcquisizioniClass, String> farmacoColumn;
    @FXML
    private TableColumn<AcquisizioniClass, Integer> doseColumn;
    @FXML
    private TableColumn<AcquisizioniClass, String> sintomiColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabellaAcquisizioni.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        mese.setOnAction(this::controlCheck);
        settimana.setOnAction(this::controlCheck);
        settimana.setSelected(true);
        initializeLineChart();

    }

    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;
        loadAcquisizioni();
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

    private void loadAcquisizioni() {
        try {
            DataModel dataModel = DataModel.getInstance();
            ObservableList<AcquisizioniClass> acquisizioniList = dataModel.getAcquisizioni(matricola_P);

            matricolaColumn.setCellValueFactory(new PropertyValueFactory<>("matricola"));
            dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
            oraColumn.setCellValueFactory(new PropertyValueFactory<>("ora"));
            sbpColumn.setCellValueFactory(new PropertyValueFactory<>("sbp"));
            dbpColumn.setCellValueFactory(new PropertyValueFactory<>("dbp"));
            farmacoColumn.setCellValueFactory(new PropertyValueFactory<>("farmaco"));
            doseColumn.setCellValueFactory(new PropertyValueFactory<>("dose"));
            sintomiColumn.setCellValueFactory(new PropertyValueFactory<>("sintomi"));

            tabellaAcquisizioni.setItems(acquisizioniList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeStage(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}