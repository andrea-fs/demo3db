package com.code.demo3db;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Contatti implements Initializable {
    private String matricola = "";
    private  Model model;
    private String medicoAssociato;
    @FXML
    private Label mail;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mail.setText("e-mail" + matricola);
        testomail(matricola);

    }
    public void initializeData(String nomeUtente){
        matricola = nomeUtente;
        System.out.println("qui"+matricola);

    }
    public void testomail(String matricola){
        System.out.println(matricola);
        try {
            model = Model.getInstance();
            medicoAssociato = model.getMedicoAssociato(matricola);
            System.out.println("Medico associato: " + medicoAssociato);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(medicoAssociato != null && !medicoAssociato.isEmpty()){
            mail.setText(medicoAssociato + "@medico.com");
        }
    }
}
