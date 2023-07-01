package com.code.demo3db;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FattoriDiRischio implements Initializable {
    private String matricola_M;
    private String matricola_P;
    @FXML
    private TextField fattoriDiRischio;
    @FXML
    private TextField coomorbidita;
    @FXML
    private TextField patologie;
    @FXML
    private TextField altro;
    @FXML
    private Button insert;
    @FXML
    private TableView<FattoreRischio> tabella;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaMatricola;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaFattoriRischio;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaPatologia;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaComorbidita;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaAltro;
    @FXML
    private TableColumn<FattoreRischio, LocalDateTime> colonnaData;
    @FXML
    private TableColumn<FattoreRischio, String> colonnaMatricolaMedico;
    @FXML
    private Label ultimoFattore;
    @FXML
    private Label ultimoPatologia;
    @FXML
    private Label ultimoComorbidita;
    @FXML
    private Label ultimoAltro;
    @FXML
    private Button misurButton;

    DatiPazienteModel model;
    public void initializeData(String nomeUtente, String paziente) {
        matricola_M = nomeUtente;
        matricola_P = paziente;
        try {
            model = DatiPazienteModel.getInstance();
            caricaFattoriRischio();
            System.out.println("siamo qui");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colonnaMatricola.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricolaPaziente()));
        colonnaFattoriRischio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFattoriRischio()));
        colonnaPatologia.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatologie()));
        colonnaComorbidita.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComorbidita()));
        colonnaAltro.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAltro()));
        colonnaData.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getData()));
        colonnaMatricolaMedico.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMedico()));

    }
    public void inserRaw(){
        try {
            model = DatiPazienteModel.getInstance();
            model.insert_raw(matricola_P,fattoriDiRischio.getText(),patologie.getText(),coomorbidita.getText(),altro.getText(),matricola_M, LocalDate.now());
            clearFields();
            caricaFattoriRischio();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void caricaFattoriRischio() {
        List<FattoreRischio> fattoriRischioList = null;
        if (model != null) {
            tabella.getItems().clear();
            fattoriRischioList = model.getFattoriRischioByMatricola(matricola_P);
            tabella.getItems().addAll(fattoriRischioList);
        }
        if (!fattoriRischioList.isEmpty()) {
            int lastIndex = fattoriRischioList.size() - 1;
            FattoreRischio ultimoFattoreRischio = fattoriRischioList.get(lastIndex);
            ultimoFattore.setText(ultimoFattoreRischio.getFattoriRischio());
            ultimoPatologia.setText(ultimoFattoreRischio.getPatologie());
            ultimoComorbidita.setText(ultimoFattoreRischio.getComorbidita());
            ultimoAltro.setText(ultimoFattoreRischio.getAltro());
        } else {
            // Se non ci sono dati, reimposta le etichette come vuote
            ultimoFattore.setText("");
            ultimoPatologia.setText("");
            ultimoComorbidita.setText("");
            ultimoAltro.setText("");
        }
    }
    private void clearFields() {
        fattoriDiRischio.clear();
        coomorbidita.clear();
        patologie.clear();
        altro.clear();
    }

    /*public void backRiepilogo (ActionEvent event) {

        //TODO Quando chiudi con indietro ha senso salvare?
        inserRaw();


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

     */

    public void goToRiepilogo (ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RiepilogoMedico.fxml"));
        Parent fxml = loader.load();
        RiepilogoMedico riepilogoMedico = loader.getController();
        riepilogoMedico.initializeData(matricola_M, matricola_P);
        Image icon = new Image(getClass().getResourceAsStream("/icona.png"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxml));
        stage.getIcons().add(icon);
        stage.show();
        stage.setTitle("Fattori di Rischio");

    }

}
