package com.timesystem.mod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dlaird on 12/25/14.
 */
public class DatabaseCreater {

    private Connection connection;

    public DatabaseCreater() {

    }

    public void create(String server, String database) {
        String url = null;

        //If databasefile exists connect to it else create it
        System.err.println("Database Exists");
        url = "jdbc:h2:tcp://" + server + "/~/" + database;

        try {
            Class.forName("org.h2.Driver");
            try {
                connection = DriverManager.getConnection(url);
                System.out.println("Connected to db");

                //Create tables
                createTimeTable();
                createEmployeeTable();

            } catch (SQLException s) {
                s.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception \n" + e);
        }

    }

    public void createTimeTable() {

        Statement createTimeTable = null;
        String createTimeQuery = "CREATE TABLE Timerecords(recordId int auto_increment, name VARCHAR(255), inTime TIMESTAMP, outTime TIMESTAMP, note VARCHAR(255))";

        try {
            createTimeTable = connection.createStatement();
            createTimeTable.executeUpdate(createTimeQuery);
        } catch (SQLException s) {
            System.out.println(createTimeQuery);
            s.printStackTrace();
        } finally {
            if (createTimeTable != null) {
                try {
                    createTimeTable.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }

    public void createEmployeeTable() {
        Statement createEmployeeTable = null;
        String createEmployeeTableQuery = "CREATE TABLE Employees(employeeId int auto_increment, name VARCHAR(255), pin int, manager int, email varchar(255)); INSERT INTO Employees VALUES (1, 'Administrator', '1234', 1, NULL)";

        try {
            createEmployeeTable = connection.createStatement();
            createEmployeeTable.executeUpdate(createEmployeeTableQuery);
        } catch (SQLException s) {
            System.out.println(createEmployeeTableQuery);
            s.printStackTrace();
        } finally {
            if (createEmployeeTable != null) {
                try {
                    createEmployeeTable.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }
}
