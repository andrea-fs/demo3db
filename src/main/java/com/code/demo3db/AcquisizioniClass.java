package com.code.demo3db;

import java.time.LocalDate;

public class AcquisizioniClass {
    private String matricola;
    private LocalDate data;
    private int ora;
    private int sbp;
    private int dbp;
    private String farmaco;
    private int dose;
    private String sintomi;

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getOra() {
        return ora;
    }

    public void setOra(int ora) {
        this.ora = ora;
    }

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    public String getFarmaco() {
        return farmaco;
    }

    public void setFarmaco(String farmaco) {
        this.farmaco = farmaco;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getSintomi() {
        return sintomi;
    }

    public void setSintomi(String sintomi) {
        this.sintomi = sintomi;
    }
}