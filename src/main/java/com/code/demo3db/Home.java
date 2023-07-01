package com.code.demo3db;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private Label alertTreGiorni;
    @FXML
    private Label terapia1;
    @FXML
    private Label terapia2;
    @FXML
    private Label terapia3;
    @FXML
    private Label terapia4;
    @FXML
    private Label quantiTerapia1;
    @FXML
    private Label quantiTerapia2;
    @FXML
    private Label quantiTerapia3;
    @FXML
    private Label quantiTerapia4;
    @FXML
    private Circle cerchio;
    @FXML
    private Button caricaTerapie;
    private TherapyModel model;
    private DataModel datamodel;
    private String matricola;
    private String farmacoT1;
    private String farmacoT2;
    private String farmacoT3;
    private String farmacoT4;
    private int daFareT1 = 0;
    private int daFareT2 = 0;

    private int daFareT3 = 0;

    private int daFareT4 = 0;

    private LocalDate dataOggi = LocalDate.now();;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            model = TherapyModel.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            datamodel = DataModel.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        terapia1.setText(null);
        terapia2.setText(null);
        terapia3.setText(null);
        terapia4.setText(null);
        quantiTerapia1.setText(null);
        quantiTerapia2.setText(null);
        quantiTerapia3.setText(null);
        quantiTerapia4.setText(null);
        cerchio.setFill(Color.DARKRED);
        alertTreGiorni.setText("");
    }
    public void initializeData(String nomeUtente) {
        matricola = nomeUtente;
        //carica();
        try {
            if(!datamodel.checkTreGiorni(matricola)){
                alertTreGiorni.setText("Attenzione! sono 3 giorni che non inserisci dati!");
                alertTreGiorni.setTextFill(Color.DARKRED);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void carica(){
        int acquisizioniTerapia1 = 0;
        int acquisizioniTerapia2 = 0;
        int acquisizioniTerapia3 = 0;
        int acquisizioniTerapia4 = 0;

        try {
            List<TerapiaClass> terapie = model.getTerapieByMatricola(matricola);

            if (terapie.size() >= 1) {
                TerapiaClass terapia1 = terapie.get(0);
                setTerapiaLabel(this.terapia1, terapia1);
                farmacoT1 = terapia1.getFarmaco();
                daFareT1 = terapia1.getAcquisizioni();
            }

            if (terapie.size() >= 2) {
                TerapiaClass terapia2 = terapie.get(1);
                setTerapiaLabel(this.terapia2, terapia2);
                farmacoT2 = terapia2.getFarmaco();
                daFareT2 = terapia2.getAcquisizioni();

            }

            if (terapie.size() >= 3) {
                TerapiaClass terapia3 = terapie.get(2);
                setTerapiaLabel(this.terapia3, terapia3);
                farmacoT3 = terapia3.getFarmaco();
                daFareT3 = terapia3.getAcquisizioni();

            }

            if (terapie.size() >= 4) {
                TerapiaClass terapia4 = terapie.get(3);
                setTerapiaLabel(this.terapia4, terapia4);
                farmacoT4 = terapia4.getFarmaco();
                daFareT4 = terapia4.getAcquisizioni();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {
            acquisizioniTerapia1 = datamodel.countAcquisizioni(farmacoT1, LocalDate.now());
            if (acquisizioniTerapia1 != 0) {
                quantiTerapia1.setText("Fatte: " + acquisizioniTerapia1 + "/" + daFareT1);
            }
            acquisizioniTerapia2 = datamodel.countAcquisizioni(farmacoT2, LocalDate.now());
            if (acquisizioniTerapia2 != 0) {
                quantiTerapia2.setText("Fatte: " + acquisizioniTerapia2 + "/" + daFareT2);
            }
            acquisizioniTerapia3 = datamodel.countAcquisizioni(farmacoT3, LocalDate.now());
            if (acquisizioniTerapia3 != 0) {
                quantiTerapia3.setText("Fatte: " + acquisizioniTerapia3 + "/" + daFareT3);
            }
            acquisizioniTerapia4 = datamodel.countAcquisizioni(farmacoT4, LocalDate.now());
            if (acquisizioniTerapia4 != 0) {
                quantiTerapia4.setText("Fatte: " + acquisizioniTerapia4 + "/" + daFareT4);
            }
            //System.out.println("we"+                  acquisizioniTerapia1 + acquisizioniTerapia2 + acquisizioniTerapia3 + acquisizioniTerapia4
            //+ farmacoT1+ daFareT1 + farmacoT2 + farmacoT3 + farmacoT4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (acquisizioniTerapia1 == daFareT1 && acquisizioniTerapia2 == daFareT2 && acquisizioniTerapia3 == daFareT3 && acquisizioniTerapia4 == daFareT4){cerchio.setFill(Color.GREEN);}

    }
    private void setTerapiaLabel(Label label, TerapiaClass terapia) {
        String text = "Data fine: " + terapia.getDataFine() +
                "  Farmaco: " + terapia.getFarmaco() +
                "  Dose: " + terapia.getDose() +
                "  Acquisizioni giornaliere: " + terapia.getAcquisizioni();

        label.setText(text);
    }

}
