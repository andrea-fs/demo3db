package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.beans.binding.Bindings;

import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class InserimentoPaziente implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label nomeUtenteLabel;
    private String nomeUtente;
    @FXML
    private Rectangle rettangolo;
    private  Model model;
    @FXML
    private Label nome;
    @FXML
    private Label cognome;
    @FXML
    private Spinner<Integer> SBP;
    @FXML
    private Spinner<Integer> DBP;


    public void initializeData(String nomeUtente, String nome, String cognome) {

        // TODO
        this.nomeUtente = nomeUtente;
        nomeUtenteLabel.setText(nomeUtente);
        this.nome.setText(nome);
        this.cognome.setText(cognome);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueSBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(80, 190, 120);
        SBP.setValueFactory(valueSBP);
        SpinnerValueFactory<Integer> valueDBP
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 130, 80);
        DBP.setValueFactory(valueDBP);
    }

}
