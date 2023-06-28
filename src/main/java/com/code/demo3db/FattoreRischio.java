package com.code.demo3db;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FattoreRischio {
    private String matricolaPaziente;
    private String fattoriRischio;
    private String patologie;
    private String comorbidita;
    private String altro;
    private String medico;
    private LocalDateTime data;


    public FattoreRischio(String matricolaPaziente, String fattoriRischio, String patologie, String comorbidita, String altro, String medico, LocalDateTime data) {
        this.matricolaPaziente = matricolaPaziente;
        this.fattoriRischio = fattoriRischio;
        this.patologie = patologie;
        this.comorbidita = comorbidita;
        this.altro = altro;
        this.medico = medico;
        this.data = data;
    }

    public String getMatricolaPaziente() {
        return matricolaPaziente;
    }

    public void setMatricolaPaziente(String matricolaPaziente) {
        this.matricolaPaziente = matricolaPaziente;
    }

    public String getFattoriRischio() {
        return fattoriRischio;
    }

    public void setFattoriRischio(String fattoriRischio) {
        this.fattoriRischio = fattoriRischio;
    }

    public String getPatologie() {
        return patologie;
    }

    public void setPatologie(String patologie) {
        this.patologie = patologie;
    }

    public String getComorbidita() {
        return comorbidita;
    }

    public void setComorbidita(String comorbidita) {
        this.comorbidita = comorbidita;
    }

    public String getAltro() {
        return altro;
    }

    public void setAltro(String altro) {
        this.altro = altro;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}