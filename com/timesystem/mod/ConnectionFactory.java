package com.timesystem.mod;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    //Used for MSSQL
    public static Connection getMSSQLConnection(String server, String port, String username, String password, String database) {
        Connection connection = null;

        String url = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to db");
                JOptionPane.showMessageDialog(null, "Connected!");
            } catch (SQLException s) {
                s.printStackTrace();
                JOptionPane.showMessageDialog(null, "Couldn't connect please verify settings");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception \n" + e);
        }
        return connection;
    }
}
