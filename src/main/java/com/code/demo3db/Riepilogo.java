package com.code.demo3db;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Riepilogo implements Initializable {
    @FXML
    private TableView tabellaAcquisizioni;
    private String matricola;
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabellaAcquisizioni.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
    public void initializeData(String matricola){
        this.matricola = matricola;
        loadAcquisizioni();
    }
    private void loadAcquisizioni() {

        try {
            DataModel dataModel = DataModel.getInstance();
            ObservableList<AcquisizioniClass> acquisizioniList = dataModel.getAcquisizioni(matricola);

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
}
