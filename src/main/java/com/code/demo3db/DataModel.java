package com.code.demo3db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class DataModel {
    private Connection conn;
    private static DataModel dataModelInstance = null;
    public void log(Object o){
        System.out.println(o);
    }
    public Connection connessione(){
        conn = null;
        String dbname = "ApplicationDB";
        String username = "avnadmin";
        String password = "AVNS_34XZTsENHTu7KB5yZ1v";

        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://pg-1fbe096a-niccolozen5-8565.aivencloud.com:16963/"+dbname,username,password);
            if (conn != null){
                System.out.println("Connection established");
            }
            else{
                System.out.println("Connection failed");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }
    DataModel() throws SQLException {
        connessione();
        if (tableExists("dati"))
        {
            log("fattori table exists");
        }
        else {
            log("fattori table DO NOT exists");
            resetDatiTable();
        }
    }
    public static synchronized DataModel getInstance() throws SQLException
    {
        if (dataModelInstance == null)
            dataModelInstance = new DataModel();

        return dataModelInstance;
    }
    public ResultSet runQuery(String q) throws SQLException {
        //PreparedStatement ps = conn.prepareStatement(q);
        //return ps.executeQuery();
        Statement stmt  = conn.createStatement();
        ResultSet rs = null;
        rs = stmt.executeQuery(q);
        return rs;
    }

    public void runStatement(String s) throws SQLException {

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(s);

    }
    public void resetDatiTable() throws SQLException{
        String s = "CREATE TABLE IF NOT EXISTS dati (" +
                "matricola TEXT, " +
                "data DATE, " +
                "ora INTEGER, " +
                "SBP INTEGER, " +
                "DBP INTEGER, " +
                "farmaco TEXT, " +
                "dose INTEGER, " +
                "sintomi TEXT, " +
                "PRIMARY KEY (matricola, data, ora, farmaco)" +
                ")";

        log(s);
        runStatement(s);
    }


    public boolean tableExists(String table_name) throws SQLException {
        String q = "SELECT * FROM information_schema.tables WHERE table_name = '"+table_name+"'";
        log(q);
        ResultSet rs = runQuery(q);
        System.out.println("Si");
        return rs.next();
    }
    private String getDateColumnType() {
        return "DATE";
    }
    public void insertData(String matricola, LocalDate date, int time, int SBP, int DBP, String farmaco, int dose, String sintomi) throws SQLException {
        String insertQuery = "INSERT INTO dati (matricola, data, ora, SBP, DBP, farmaco, dose, sintomi) " +
                "VALUES ('" + matricola + "', '" + date + "', '" + time + "', " + SBP + ", " + DBP + ", '" + farmaco + "', '" + dose + "', '" + sintomi + "')";

        runStatement(insertQuery);
    }
/*
    public boolean controlloinserimento (String matricola,  LocalDate date, int time) throws SQLException{
        String q = "SELECT * FROM dati WHERE matricola = '" + matricola + "' AND data = '" + date + "' AND ora = '" + time + "'"; //TODO se hai tempo inserimento parm x sicurezza
        log(q);

        ResultSet rs = runQuery(q);
        boolean esiste = rs.next();
        if(!esiste){
            System.out.println("Va bene");
            return true;
        }
        else{
            System.out.println("non va bene");
            return false;
        }
    }*/
    public boolean controlloinserimento(String matricola, LocalDate date, int time, String farmaco) throws SQLException {
        String query = "SELECT * FROM dati WHERE matricola = ? AND data = ? AND ora = ? AND farmaco = ?";
        log(query);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, matricola);
            statement.setDate(2, java.sql.Date.valueOf(date));
            statement.setInt(3, time);
            statement.setString(4, farmaco);

            try (ResultSet rs = statement.executeQuery()) {
                boolean esiste = rs.next();
                if (!esiste) {
                    System.out.println("Va bene");
                    return true;
                } else {
                    System.out.println("non va bene");
                    return false;
                }
            }
        }
    }
    public int countAcquisizioni(String farmaco, LocalDate data) throws SQLException {
        String query = "SELECT COUNT(*) FROM dati WHERE farmaco = '" + farmaco + "' AND data = '" + data + "'";
        log(query);

        ResultSet rs = runQuery(query);
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }
    public ObservableList<Measurement> getMeasurements(String matricola, boolean isWeekSelected) throws SQLException {
        ObservableList<Measurement> measurements = FXCollections.observableArrayList();

        LocalDate startDate;
        if (isWeekSelected) {
            // Ottieni la data di inizio della settimana corrente
            startDate = LocalDate.now().with(DayOfWeek.MONDAY);
        } else {
            // Ottieni la data di inizio del mese corrente
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        String query = "SELECT data, SBP, DBP FROM dati WHERE matricola = ? AND data >= ? ORDER BY data";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, matricola);
        statement.setDate(2, java.sql.Date.valueOf(startDate));

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            LocalDate date = rs.getDate("data").toLocalDate();
            int sbp = rs.getInt("SBP");
            int dbp = rs.getInt("DBP");

            Measurement measurement = new Measurement(date, sbp, dbp);
            measurements.add(measurement);
        }

        return measurements;
    }
    public boolean checkTreGiorni(String matricola) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(2); // Data di inizio dei 3 giorni precedenti

        String query = "SELECT * FROM dati WHERE matricola = '" + matricola + "'AND data >= '" + startDate + "' AND data <= '" + today + "'";
        ResultSet rs = runQuery(query);
        boolean hasInsertions = rs.next();

        return hasInsertions;

    }
}
