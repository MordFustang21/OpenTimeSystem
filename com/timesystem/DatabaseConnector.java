package com.timesystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static DatabaseConnector instance;
    private Connection connection;

    public DatabaseConnector() {

    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }

        return instance;
    }

    //Used for MSSQL
    public Connection makeConnection(String server, String port, String username, String password, String database) {

        String url = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to db");
            } catch (SQLException s) {
                s.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception \n" + e);
        }
        return connection;
    }

    //Used for MySql
    public Connection makeMysqlConnection(String server, String port, String username, String password, String database) {

        String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?autoReconnect=true";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to db");
            } catch (SQLException s) {
                s.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return connection;
    }

}
