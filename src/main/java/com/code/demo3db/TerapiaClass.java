package com.code.demo3db;

import java.time.LocalDate;

public class TerapiaClass {
    private LocalDate dataFine;
    private String farmaco;
    private int dose;
    private int acquisizioni;

    public TerapiaClass(LocalDate dataFine, String farmaco, int dose, int acquisizioni) {
        this.dataFine = dataFine;
        this.farmaco = farmaco;
        this.dose = dose;
        this.acquisizioni = acquisizioni;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public String getFarmaco() {
        return farmaco;
    }

    public int getDose() {
        return dose;
    }

    public int getAcquisizioni() {
        return acquisizioni;
    }
}
