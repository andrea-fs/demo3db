package com.code.demo3db;

import com.code.demo3db.Paziente;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ScegliPaziente implements Initializable {
    private MedicoInterfaccia medicoInterfacciaController;

    @FXML
    private TableView<Paziente> tabella;
    @FXML
    private TableView<Alert> alertTable;
    @FXML
    private TableColumn<Alert, String> colonnaMatricolaAlert;
    @FXML
    private TableColumn<Alert, String> colonnaCategoria;
    @FXML
    private TableColumn<Paziente, String> colonnaMatricola;
    @FXML
    private TableColumn<Paziente, String> colonnaNome;
    @FXML
    private TableColumn<Paziente, String> colonnaCognome;
    private String matricola;
    @FXML
    private Label selezionato;
    @FXML
    private Label scegliPazienteAdatto;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabella.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colonnaMatricola.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricola()));
        colonnaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colonnaCognome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCognome()));
        colonnaMatricolaAlert.setCellValueFactory(data -> data.getValue().matricolaProperty());
        colonnaCategoria.setCellValueFactory(data -> data.getValue().categoriaProperty());
        // distingui la grave
        colonnaCategoria.setCellFactory(new Callback<TableColumn<Alert, String>, TableCell<Alert, String>>() {
            @Override
            public TableCell<Alert, String> call(TableColumn<Alert, String> column) {
                return new TableCell<Alert, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null && !empty) {
                            setText(item);

                            if (item.equals("Grado 3 grave") || item.equals("Sistolica isolata")) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        caricaPazienti();

        selezionato.setText("clicca nella tabella un paziente");
        scegliPazienteAdatto.setText("");


    }
    public void initializeData(String matricola) {
        this.matricola = matricola;
        System.out.println("             "+matricola);
        //caricaPazienti();
        caricaAlert(matricola);


    }
    private void caricaPazienti() {

        try {
            Model model = Model.getInstance();
            List<Paziente> pazienti = model.getPazientiByMedico();
            tabella.getItems().addAll(pazienti);

            tabella.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    Paziente pazienteSelezionato = tabella.getSelectionModel().getSelectedItem();
                    if (pazienteSelezionato != null) {
                        String matricolaSelezionata = pazienteSelezionato.getMatricola();
                        medicoInterfacciaController.setMatricola_P(matricolaSelezionata);
                        selezionato.setText(matricolaSelezionata);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void caricaAlert(String matricola) {
        try {
            AlertModel model = AlertModel.getInstance();
            List<Alert> alert = model.getAlertsByCategory(matricola);

            alertTable.getItems().clear();
            alertTable.getItems().addAll(alert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMedicoInterfacciaController(MedicoInterfaccia controller) {
        this.medicoInterfacciaController = controller;
    }
    public void selezionarePazienteAdatto(){
        scegliPazienteAdatto.setText("Seleziona un paziente adatto");
        scegliPazienteAdatto.setTextFill(Color.DARKRED);
    }
}
