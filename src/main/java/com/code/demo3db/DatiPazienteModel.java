package com.code.demo3db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DatiPazienteModel {
    private Connection conn;
    private static DatiPazienteModel dataModelInstance = null;
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
    DatiPazienteModel() throws SQLException {
        connessione();
        if (tableExists("fattori"))
        {
            log("fattori table exists");
        }
        else {
            log("fattori table DO NOT exists");
            resetDatiTable();
        };
    }
    public static synchronized DatiPazienteModel getInstance() throws SQLException
    {
        if (dataModelInstance == null)
            dataModelInstance = new DatiPazienteModel();

        return dataModelInstance;
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
        String s = "CREATE TABLE IF NOT EXISTS fattori (" +
                "matricola VARCHAR(255), " +
                "Fattori_di_rischio VARCHAR(255), " +
                "Patologie VARCHAR(255), " +
                "Comorbidita VARCHAR(255), " +
                "Altro VARCHAR(255), " +
                "Medico VARCHAR(255), " +
                "data DATE, " +
                "PRIMARY KEY (matricola, data, medico, Fattori_di_rischio, Patologie, Comorbidita, Altro)" +
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
    public void insert_raw(String matricola, String fattori_di_rischio, String patologie, String comorbidita, String altro, String medico, LocalDate data){

        try{
            System.out.println("inserimento");
            String q = String.format("insert into fattori(Matricola,Fattori_di_rischio,Patologie,Comorbidita,Altro,Medico,data) values('%s', '%s', '%s', '%s', '%s', '%s', '%s');", matricola, fattori_di_rischio, patologie, comorbidita, altro, medico, data);
            log(q);
            runStatement(q);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("non inserito");
        }
    }
    public List<FattoreRischio> getFattoriRischioByMatricola(String matricola) {
        List<FattoreRischio> fattoriRischioList = new ArrayList<>();

        try {
            String query = "SELECT * FROM fattori WHERE matricola = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, matricola);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String matricolaPaziente = rs.getString("matricola");
                String fattoriRischio = rs.getString("Fattori_di_rischio");
                String patologie = rs.getString("Patologie");
                String comorbidita = rs.getString("Comorbidita");
                String altro = rs.getString("Altro");
                String medico = rs.getString("Medico");
                LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();

                FattoreRischio fattoreRischio = new FattoreRischio(matricolaPaziente, fattoriRischio, patologie, comorbidita, altro, medico, data);
                fattoriRischioList.add(fattoreRischio);
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fattoriRischioList;
    }
}
