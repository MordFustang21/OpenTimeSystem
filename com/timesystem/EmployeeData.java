package com.timesystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dlaird on 9/8/14.
 */
public class EmployeeData {
    Connection connection;

    public EmployeeData(Connection connection){
        this.connection = connection;
    }

    public void updateEmployeeRecord(String rowid, String name, String pin, int manager, String email) {
        Statement writeTimeStatement = null;
        String updateTimeQuery = "update Demployee set Name = '" + name + "', pin = '" + pin + "', Manager = '" + manager + "', email = '" + email + "' where employeeId = '" + rowid + "'";

        try {
            writeTimeStatement = connection.createStatement();
            writeTimeStatement.executeUpdate(updateTimeQuery);
        } catch (SQLException s) {
            System.out.println(updateTimeQuery);
            System.out.println(s.getMessage());
        } finally {
            if (writeTimeStatement != null) {
                try {
                    writeTimeStatement.close();
                } catch (SQLException s) {
                    System.out.println(s.getMessage());
                }
            }
        }
    }

    public void deleteRecord(String rowid) {
        Statement deleteTimeStatement = null;
        String deleteRecordQuery = "delete from Demployee where employeeId = '" + rowid + "'";


        try {
            deleteTimeStatement = connection.createStatement();
            deleteTimeStatement.executeUpdate(deleteRecordQuery);
        } catch (SQLException s) {
            System.out.println(deleteRecordQuery);
            System.out.println(s.getMessage());
        } finally {
            if (deleteTimeStatement != null) {
                try {
                    deleteTimeStatement.close();
                } catch (SQLException s) {
                    System.out.println(s.getMessage());
                }
            }
        }
    }
}
