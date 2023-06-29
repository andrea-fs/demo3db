package com.code.demo3db;
import java.time.LocalDate;

public class Measurement {
    private LocalDate date;
    private int sbp;
    private int dbp;

    public Measurement(LocalDate date, int sbp, int dbp) {
        this.date = date;
        this.sbp = sbp;
        this.dbp = dbp;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSbp() {
        return sbp;
    }

    public int getDbp() {
        return dbp;
    }
}
