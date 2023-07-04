package com.code.demo3db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlertModel {
    private Connection conn;

    private static AlertModel alertModel = null;
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
    AlertModel() throws SQLException {
        connessione();
        if (tableExists("alert"))
        {
            log("alert table exists");
        }
        else {
            log("alert table DO NOT exists");
            resetDatiTable();
        }
    }
    public static synchronized AlertModel getInstance() throws SQLException
    {
        if (alertModel == null)
            alertModel = new AlertModel();

        return alertModel;
    }
    public ResultSet runQuery(String q) throws SQLException {
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
        String s = "DROP TABLE IF EXISTS alert;" +
                "CREATE TABLE alert (" +
                "id SERIAL PRIMARY KEY, " +
                "paziente VARCHAR(255), " +
                "medico VARCHAR(255), " +
                "categoria VARCHAR(255), " +
                "SBP INTEGER, " +
                "DBP INTEGER, " +
                "data DATE" +
                ");";

        log(s);
        runStatement(s);
    }


    public boolean tableExists(String table_name) throws SQLException {
        String q = "SELECT * FROM information_schema.tables WHERE table_name = '"+table_name+"'";
        log(q);
        ResultSet rs = runQuery(q);
        System.out.println("Si alert");
        return rs.next();
    }
    public void insertAlert(String paziente, String medico, String categoria, int sbp, int dbp, LocalDate data) throws SQLException {
        String q = "INSERT INTO alert (paziente, medico, categoria, SBP, DBP, data) " +
                "VALUES ('" + paziente + "', '" + medico + "', '" + categoria + "', " + sbp + ", " + dbp + ", '" + data + "')";

        log(q);
        runStatement(q);
    }
    public List<Alert> getAlertsByCategory(String medicoMatricola) throws SQLException {
        List<Alert> alerts = new ArrayList<>();
        LocalDate unaSettimanaFa = LocalDate.now().minusWeeks(1);


        String query = "SELECT * FROM alert WHERE medico = '" + medicoMatricola + "' AND data > '" + unaSettimanaFa + "'"; //AND categoria IN ('Grado 3 grave', 'Grado 2 moderata', 'Sistolica isolata')
        ResultSet resultSet = runQuery(query);
        System.out.println(medicoMatricola + "matricola passata");

        while (resultSet.next()) {
            String matricola = resultSet.getString("paziente");
            String categoria = resultSet.getString("categoria");
            Alert alert = new Alert(matricola, categoria);
            alerts.add(alert);
        }

        return alerts;
    }

}
