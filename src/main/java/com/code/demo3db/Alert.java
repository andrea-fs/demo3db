package com.code.demo3db;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;



public class Alert {
    private StringProperty matricola;
    private StringProperty categoria;
    private ObjectProperty<LocalDate> data;


    public Alert(String matricola, String categoria, LocalDate data) {
        this.matricola = new SimpleStringProperty(matricola);
        this.categoria = new SimpleStringProperty(categoria);
        this.data = new SimpleObjectProperty<>(data);

    }

    public String getMatricola() {
        return matricola.get();
    }

    public StringProperty matricolaProperty() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola.set(matricola);
    }

    // non usato
    public String getCategoria() {
        return categoria.get();
    }

    public StringProperty categoriaProperty() {
        return categoria;
    }

    //non usato
    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    public LocalDate getData() {
        return data.get();
    }

    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data.set(data);
    }
}