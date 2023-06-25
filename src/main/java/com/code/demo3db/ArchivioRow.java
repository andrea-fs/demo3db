package com.code.demo3db;

import javafx.beans.property.SimpleStringProperty;

public class ArchivioRow {
    private SimpleStringProperty matricola;
    private SimpleStringProperty password;
    private SimpleStringProperty nome;
    private SimpleStringProperty cognome;
    private SimpleStringProperty medicoPaziente;
    private SimpleStringProperty medicoAssociato;



    public ArchivioRow(String matricola, String password, String nome, String cognome, String medicoPaziente, String medico_associato) {
        this.matricola = new SimpleStringProperty(matricola);
        this.password = new SimpleStringProperty(password);
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.medicoPaziente = new SimpleStringProperty(medicoPaziente);
        this.medicoAssociato = new SimpleStringProperty(medico_associato);
    }

    public String getMatricola() {
        return matricola.get();
    }

    public SimpleStringProperty matricolaProperty() {
        return matricola;
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public String getCognome() {
        return cognome.get();
    }

    public SimpleStringProperty cognomeProperty() {
        return cognome;
    }

    public String getMedicoPaziente() {
        return medicoPaziente.get();
    }

    public SimpleStringProperty medicoPazienteProperty() {
        return medicoPaziente;
    }

    public String getMedicoAssociato() {
        return medicoAssociato.get();
    }

    public SimpleStringProperty medicoAssociatoProperty() {
        return medicoAssociato;
    }
}