package com.code.demo3db;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Contatti implements Initializable {
    public String matricola_M;
    public String matricola_P;
    @FXML
    private TableView<Messaggio> tabella;
    @FXML
    private TableColumn<Messaggio, String> testoColonna;
    @FXML
    private TableColumn<Messaggio, String> dataColonna;
    @FXML
    private TextArea newMessage;
    @FXML
    private Button insertMessaggio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testoColonna.setCellValueFactory(new PropertyValueFactory<>("testo"));
        dataColonna.setCellValueFactory(new PropertyValueFactory<>("data"));
    }
    public void initializeData(String nomeUtente, String medico) {
        matricola_M = medico;
        matricola_P = nomeUtente;
        visualizzaMessaggi();

    }
    public void visualizzaMessaggi() {
        try {
            MessageModel messageModel = MessageModel.getInstance();
            List<Messaggio> messaggi = messageModel.getMessaggiTraMedicoEPaziente(matricola_M, matricola_P);

            tabella.getItems().clear();

            tabella.getItems().addAll(messaggi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void insertMessaggio() {
        try {
            MessageModel messageModel = MessageModel.getInstance();
            messageModel.insertMessage("p", matricola_M, matricola_P, newMessage.getText());

            visualizzaMessaggi();

            newMessage.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        visualizzaMessaggi();
    }


}
