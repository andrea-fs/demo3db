package com.code.demo3db;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class InserimentoPaziente {
    @FXML
    private Label nomeUtenteLabel;
    private String nomeUtente;


    public void initializeData(String nomeUtente) {

        // TODO
        this.nomeUtente = nomeUtente;
        nomeUtenteLabel.setText(nomeUtente);

    }
}
