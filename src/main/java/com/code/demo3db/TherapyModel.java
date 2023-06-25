package com.code.demo3db;

import java.sql.*;
import java.time.LocalDate;

public class TherapyModel {



    private Connection conn;
    private static com.code.demo3db.TherapyModel dataTherapyInstance = null;
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
                "PRIMARY KEY (matricola_P, data_fine)" +
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


}
