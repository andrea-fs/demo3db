package com.code.demo3db;

import javafx.collections.FXCollections;

import java.sql.*;
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
        };
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
                "PRIMARY KEY (matricola, data, ora)" +
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
        // Prepara l'istruzione di inserimento
        String insertQuery = "INSERT INTO dati (matricola, data, ora, SBP, DBP, farmaco, dose, sintomi) " +
                "VALUES ('" + matricola + "', '" + date + "', '" + time + "', " + SBP + ", " + DBP + ", '" + farmaco + "', '" + dose + "', '" + sintomi + "')";

        // Esegui l'istruzione di inserimento
        runStatement(insertQuery);
    }

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
}
