package com.code.demo3db;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alert {
    private StringProperty matricola;
    private StringProperty categoria;

    public Alert(String matricola, String categoria) {
        this.matricola = new SimpleStringProperty(matricola);
        this.categoria = new SimpleStringProperty(categoria);
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
}