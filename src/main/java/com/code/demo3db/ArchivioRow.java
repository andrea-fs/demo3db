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



    public String getPassword() {
        return password.get();
    }


    public String getNome() {
        return nome.get();
    }


    public String getCognome() {
        return cognome.get();
    }

    // non usato
    public String getMedicoPaziente() {
        return medicoPaziente.get();
    }


    public String getMedicoAssociato() {
        return medicoAssociato.get();
    }
}