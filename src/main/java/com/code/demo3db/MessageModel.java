package com.code.demo3db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageModel {
    private Connection conn;
    private static com.code.demo3db.MessageModel dataMessageInstance = null;
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
    MessageModel() throws SQLException {
        connessione();
        if (tableExists("chat"))
        {
            log("chat table exists");
        }
        else {
            log("chat table DO NOT exists");
            resetTherapyTable();
        }
    }
    public static synchronized com.code.demo3db.MessageModel getInstance() throws SQLException
    {
        if (dataMessageInstance == null)
            dataMessageInstance = new com.code.demo3db.MessageModel();

        return dataMessageInstance;
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
        String s = "DROP TABLE IF EXISTS chat;" +
                "CREATE TABLE chat (" +
                "id SERIAL PRIMARY KEY, " +
                "mittente CHAR(1), " +
                "medico VARCHAR(255), " +
                "paziente VARCHAR(255), " +
                "testo VARCHAR(2000), " +
                "data TIMESTAMP" +
                ");";
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
    public void insertMessage(String mittente, String medico, String paziente, String testo) throws SQLException {
        String newTesto;
        if (mittente.equals("m")) {
            newTesto = "M: " + testo;
        } else if (mittente.equals("p")) {
            newTesto = "P: " + testo;
        } else {
            // Mittente non valido
            return;
        }

        String query = "INSERT INTO chat (mittente, medico, paziente, testo, data) " +
                "VALUES ('" + mittente + "', '" + medico + "', '" + paziente + "', '" + newTesto + "', CURRENT_TIMESTAMP);";

        log(query);
        runStatement(query);
    }
    public List<Messaggio> getMessaggiTraMedicoEPaziente(String medico, String paziente) throws SQLException {
        List<Messaggio> messaggi = new ArrayList<>();

        String query = "SELECT * FROM chat WHERE medico = '" + medico + "' AND paziente = '" + paziente + "'";
        ResultSet resultSet = runQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String testo = resultSet.getString("testo");
            Timestamp data = resultSet.getTimestamp("data");
            Messaggio messaggio = new Messaggio(id, testo, data);
            messaggi.add(messaggio);
        }

        return messaggi;
    }
}
