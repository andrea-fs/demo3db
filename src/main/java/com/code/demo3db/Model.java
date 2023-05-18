package com.code.demo3db;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

public class Model {
    private Connection conn;
    private static Model single_instance = null;
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
    public static synchronized Model getInstance() throws SQLException
    {
        if (single_instance == null)
            single_instance = new Model();

        return single_instance;
    }
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
                "name VARCHAR(255) PRIMARY KEY, " +
                "password VARCHAR(200)" +
                ");";

        log(s);
        runStatement(s);
    }
    Model() throws SQLException {
        connessione();
        if (tableExists("archivio"))
        {
            log("archivio table exists");
        }
        else {
            log("archivio table DO NOT exists");
            resetArchivioTable();
        };
    }

    public void insert_raw(String tablename, String name, String password){

        try{
            //String query = "insert into "+tablename+"(name,password) "+"values('"+name+","+password+");";
            String q = String.format("insert into %s(name,password) values('%s', '%s');",tablename,name,password);
            log(q);
            runStatement(q);
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
        return rs.next();
    }
    public void setUser(String user) throws SQLException{
        String s = "UPDATE archivio SET name = '" + user;
        log(s);
        runStatement(s);
    }


    public boolean controlloUtente(String name, String password) throws SQLException{
        String q = "SELECT * FROM archivio WHERE name = '" + name + "' AND password = '" + password + "'";
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

}
