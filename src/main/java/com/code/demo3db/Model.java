package com.code.demo3db;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.demo3db.ArchivioRow;

// TODO controlli tuple inserimento ecc

public class Model {
    private Connection conn;
    private static Model single_instance = null;
    private ObservableList<ArchivioRow> archivioRows;


    public void log(Object o){
        System.out.println(o);
    }
    public Connection connessione(){
        conn = null;
        String dbname = "trydb";
        String username = "postgres";
        String password = "dbandre";

        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,username,password);
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
    public ObservableList<ArchivioRow> getArchivioRows() {
        return archivioRows;
    }
    public static synchronized Model getInstance() throws SQLException
    {
        if (single_instance == null)
            single_instance = new Model();

        return single_instance;
    }
    // non utilizzato
    public void createTable(String table_name){

        try{
            String query = "create table "+table_name+"(empid SERIAL,name varchar(200),address varchar(200),primary key(empid));";
            runStatement(query);
            System.out.println("Table create");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void resetArchivioTable() throws SQLException{
        String s = "DROP TABLE IF EXISTS archivio;" +
                "CREATE TABLE archivio( " +
                "matricola VARCHAR(255) PRIMARY KEY, " +
                "password VARCHAR(200), " +
                "nome VARCHAR(200), " +
                "cognome VARCHAR(200), " +
                "type VARCHAR(200), " +
                "medico_associato VARCHAR(200)" +
                ");";

        log(s);
        runStatement(s);
    }
    Model() throws SQLException {
        connessione();
        archivioRows = FXCollections.observableArrayList(); // Inizializza la lista archivioRows

        if (tableExists("archivio"))
        {
            log("archivio table exists");
            fetchArchivioData();
        }
        else {
            log("archivio table DO NOT exists");
            resetArchivioTable();
        };
    }

    public void insert_raw(String tablename, String matricola, String password, String nome, String cognome, String type, String medico_associato){

        try{
            //String query = "insert into "+tablename+"(name,password) "+"values('"+name+","+password+");";
            String q = String.format("insert into %s(matricola,password,nome,cognome,type,medico_associato) values('%s', '%s', '%s', '%s', '%s', '%s');", tablename, matricola, password, nome, cognome, type, medico_associato);
            log(q);
            runStatement(q);

            ArchivioRow row = new ArchivioRow(matricola, password, nome, cognome, type, medico_associato);
            archivioRows.add(row);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void remove_raw(String tablename, String matricola){
        try{
            //String query = "insert into "+tablename+"(name,password) "+"values('"+name+","+password+");";
            String q = String.format("DELETE FROM %s WHERE matricola = '%s';", tablename, matricola);
            log(q);
            Statement stmt = conn.createStatement();
            boolean rimosso = stmt.executeUpdate(q) > 0;
            if(rimosso) {
                archivioRows.removeIf(row -> row.getMatricola().equals(matricola));
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void runStatement(String s) throws SQLException {

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(s);

    }

    public ResultSet runQuery(String q) throws SQLException {
        //PreparedStatement ps = conn.prepareStatement(q);
        //return ps.executeQuery();
        Statement stmt  = conn.createStatement();
        ResultSet rs = null;
        rs = stmt.executeQuery(q);
        return rs;
    }

    public boolean tableExists(String table_name) throws SQLException {
        String q = "SELECT * FROM information_schema.tables WHERE table_name = '"+table_name+"'";
        log(q);
        ResultSet rs = runQuery(q);
        System.out.println("Si");
        return rs.next();
    }
    public void setUser(String user) throws SQLException{
        String s = "UPDATE archivio SET matricola = '" + user;
        log(s);
        runStatement(s);
    }// non serve
    public String getType(String matricola) throws SQLException {
        String q = "SELECT type FROM archivio WHERE matricola = '" + matricola + "'";
        log(q);
        ResultSet rs = runQuery(q);
        if (rs.next()) {
            return rs.getString("type");
        } else {
            return null;
        }
    }


    public boolean controlloUtente(String matricola, String password) throws SQLException{
        String q = "SELECT * FROM archivio WHERE matricola = '" + matricola + "' AND password = '" + password + "'";
        log(q);

        ResultSet rs = runQuery(q);
        boolean esiste = rs.next();
        if(esiste){
            System.out.println("Accessso");
            return true;
        }
        else{
            System.out.println("no");
            return false;
        }
    }

    public void fetchArchivioData() throws SQLException {
        String query = "SELECT * FROM archivio";
        ResultSet rs = runQuery(query);

        // Crea una nuova lista temporanea per i dati di archivio
        ArrayList<ArchivioRow> tempList = new ArrayList<>();

        while (rs.next()) {
            String matricola = rs.getString("matricola");
            String password = rs.getString("password");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String medicoPaziente = rs.getString("type");
            String medico_associato = rs.getString("medico_associato");

            ArchivioRow row = new ArchivioRow(matricola, password, nome, cognome, medicoPaziente, medico_associato);
            tempList.add(row);
        }

        // Sostituisci la lista archivioRows con i nuovi dati
        archivioRows = FXCollections.observableArrayList(tempList);
    }

    public String[] getDatafromMatricola(String nometabella, String matricola) throws SQLException {
        String q = String.format("SELECT nome, cognome FROM %s WHERE matricola = '%s'", nometabella,matricola);
        log(q);
        ResultSet r = runQuery(q);

        if (r.next()){
            String nome = r.getString("nome");
            String cognome = r.getString("cognome");
            return new String[]{nome, cognome};
        }
        return null;
    }
    public List<Paziente> getPazientiByMedico(String matricolaMedico) throws SQLException {
        System.out.println("ouououououo" + matricolaMedico);
        List<Paziente> pazienti = new ArrayList<>();
        String query = "SELECT matricola, nome, cognome FROM archivio WHERE medico_associato = '" + matricolaMedico + "'";
        ResultSet rs = runQuery(query);

        while (rs.next()) {
            String matricola = rs.getString("matricola");
            System.out.println("qui " + matricola);
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            Paziente paziente = new Paziente(matricola, nome, cognome);
            pazienti.add(paziente);
        }
        return pazienti;
    }

}
