package com.code.demo3db;

import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TherapyModel {


//TODO max 4 terapie
    private Connection conn;
    private static com.code.demo3db.TherapyModel dataTherapyInstance = null;
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
    TherapyModel() throws SQLException {
        connessione();
        if (tableExists("therapy"))
        {
            log("therapy table exists");
        }
        else {
            log("therapy table DO NOT exists");
            resetTherapyTable();
        };
    }
    public static synchronized com.code.demo3db.TherapyModel getInstance() throws SQLException
    {
        if (dataTherapyInstance == null)
            dataTherapyInstance = new com.code.demo3db.TherapyModel();

        return dataTherapyInstance;
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
    public void resetTherapyTable() throws SQLException{
        String s = "CREATE TABLE IF NOT EXISTS therapy (" +
                "matricola_M TEXT, " +
                "matricola_P TEXT, " +
                "data_fine DATE, " +
                "farmaco TEXT, " +
                "dose INTEGER, " +
                "acquisizioni INTEGER, " +
                "PRIMARY KEY (matricola_P, data_fine, farmaco)" +
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
    public void insertData(String matricola_M, String matricola_P, LocalDate date, String farmaco, int dose, int acquisizioni) throws SQLException {

        String insertQuery = "INSERT INTO therapy (matricola_M, matricola_P, data_fine, farmaco, dose, acquisizioni) " +
                "VALUES ('" + matricola_M + "', '" + matricola_P + "', '" + date + "', '" + farmaco + "', '" + dose + "', '" + acquisizioni + "')";
        runStatement(insertQuery);
    }

    public List<TerapiaClass> getTerapieByMatricola(String matricola) throws SQLException {
        List<TerapiaClass> terapie = new ArrayList<>();

        String query = "SELECT data_fine, farmaco, dose, acquisizioni FROM therapy WHERE matricola_P = '" + matricola + "'";
        ResultSet rs = runQuery(query);

        while (rs.next()) {
            LocalDate dataFine = rs.getDate("data_fine").toLocalDate();
            String farmaco = rs.getString("farmaco");
            int dose = rs.getInt("dose");
            int acquisizioni = rs.getInt("acquisizioni");

            TerapiaClass terapia = new TerapiaClass(dataFine, farmaco, dose, acquisizioni);
            terapie.add(terapia);
        }

        return terapie;
    }
    public List<TerapiaClass> getTerapieForTable(String matricola, boolean isMeseSelected) throws SQLException {
        List<TerapiaClass> terapie = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate minDate;
        if (isMeseSelected) { //
            minDate = currentDate.minusMonths(1);
        } else {
            minDate = currentDate.minusWeeks(1);
        }

        String query = "SELECT data_fine, farmaco, dose, acquisizioni FROM therapy " +
                "WHERE matricola_P = '" + matricola + "' " +
                "AND data_fine >= '" + minDate + "'";

        ResultSet rs = runQuery(query);

        while (rs.next()) {
            LocalDate dataFine = rs.getDate("data_fine").toLocalDate();
            String farmaco = rs.getString("farmaco");
            int dose = rs.getInt("dose");
            int acquisizioni = rs.getInt("acquisizioni");

            TerapiaClass terapia = new TerapiaClass(dataFine, farmaco, dose, acquisizioni);
            terapie.add(terapia);
        }

        return terapie;
    }
    public List<Pair<String, Integer>> getFarmaciTerapieAttive(String matricolaP) throws SQLException {
        List<Pair<String, Integer>> farmaci = new ArrayList<>();

        LocalDate today = LocalDate.now();

        String query = "SELECT farmaco, dose FROM therapy WHERE matricola_P = '" + matricolaP + "' AND data_fine > '" + today + "'";
        ResultSet rs = runQuery(query);

        while (rs.next()) {
            String farmaco = rs.getString("farmaco");
            int dose = rs.getInt("dose");
            Pair<String, Integer> farmacoDose = new Pair<>(farmaco, dose);
            farmaci.add(farmacoDose);
        }

        return farmaci;
    }
    public void eliminaTerapia(TerapiaClass terapia,String matricola) throws SQLException {
        String deleteQuery = "DELETE FROM therapy WHERE matricola_P = ? AND data_fine = ? AND farmaco = ?";
        PreparedStatement ps = conn.prepareStatement(deleteQuery);
        ps.setString(1, matricola);
        ps.setDate(2, java.sql.Date.valueOf(terapia.getDataFine()));
        ps.setString(3, terapia.getFarmaco());
        ps.executeUpdate();
    }

}
