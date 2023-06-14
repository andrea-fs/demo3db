package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.function.Predicate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public class Inserimento implements Initializable {
    //TODO controllare inserimenti già fatti da databse
    @FXML
    private Spinner<Integer> SBP;
    @FXML
    private Spinner<Integer> DBP;
    @FXML
    private DatePicker data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueSBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(80, 190, 120);
        SBP.setValueFactory(valueSBP);
        SpinnerValueFactory<Integer> valueDBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 130, 80);
        DBP.setValueFactory(valueDBP);

        data.setDayCellFactory(getDayCellFactory());
        data.setDayCellFactory(getDayCellFactory());
    }
    @FXML
    void datePicker(ActionEvent event){
        LocalDate localdate = data.getValue();

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
}
