package com.timesystem.mod;


import com.timesystem.cont.TimeFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.text.ParseException;
import java.util.Vector;

public class Databasedat {

    private Connection connection;
    private static Databasedat instance;

    public static Databasedat getInstance() {
        if (instance == null) {
            instance = new Databasedat();
        }
        return instance;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    //Handle employee information
    public String getName(String pin) {
        Statement getName = null;
        String getNameQuery = "SELECT Name FROM Employees WHERE pin = '" + pin + "'";
        System.out.println("Running getName query " + getNameQuery);


        try {
            getName = connection.createStatement();
            ResultSet getNameResult = getName.executeQuery(getNameQuery);
            getNameResult.next();
            return getNameResult.getString("Name");
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            if (getName != null) {
                try {
                    getName.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }

            }
        }

        return "Employee doesn't exist";
    }

    //Handle admin login info
    public int getAdminCredentials(String username, String password) {

        int MANAGER = 2;
        int EMPLOYEE = 1;
        int LOGIN_FAILED = 0;
        //Get username AND password
        Statement getAdmin = null;
        String getAdminQuery = "SELECT Name, pin, manager FROM Employees WHERE Name = '" + username + "' AND pin = '" + password + "'";

        try {
            getAdmin = connection.createStatement();
            System.out.println("Running getAdminQuery " + getAdminQuery);
            ResultSet getAdminResult = getAdmin.executeQuery(getAdminQuery);

            getAdminResult.next();
            //Check to see if manager
            if (getAdminResult.getInt("manager") == 1) {
                //if manager
                return MANAGER;
            } else {
                //regular employee
                return EMPLOYEE;
            }

        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            if (getAdmin != null) {
                try {
                    getAdmin.close();
                } catch (SQLException s) {

                }
            }
        }

        return LOGIN_FAILED;
    }

    public int checkForIn(String username) {
        //Get username AND password
        Statement getAdmin = null;
        String getAdminQuery = "SELECT COUNT(*) count FROM Timerecords WHERE Name = '" + username + "' AND outTime IS null AND inTime > CONVERT(date, GetDate())";

        try {
            getAdmin = connection.createStatement();
            System.out.println("Running getAdminQuery " + getAdminQuery);
            ResultSet getAdminResult = getAdmin.executeQuery(getAdminQuery);

            System.out.println(getAdminResult.next());
            int result = getAdminResult.getInt("count");

            return result;

        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            if (getAdmin != null) {
                try {
                    getAdmin.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }

        return 0;
    }

    public DefaultComboBoxModel<String> getEmployeeList(DefaultComboBoxModel<String> employeeList) {
        //Get username AND password
        Statement getEmployeeList = null;
        String getEmployeeListQuery = "SELECT distinct name FROM Timerecords";

        try {
            getEmployeeList = connection.createStatement();
            System.out.println("Running getEmployeeList " + getEmployeeListQuery);
            ResultSet getEmployeeListResult = getEmployeeList.executeQuery(getEmployeeListQuery);
            while (getEmployeeListResult.next()) {
                employeeList.addElement(getEmployeeListResult.getString("name"));

            }

        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            if (getEmployeeList != null) {
                try {
                    getEmployeeList.close();
                } catch (SQLException s) {

                }
            }
        }
        return employeeList;
    }

    //Example Dakota, 1224444, IN
    public String createRecord(String name, String state) {
        Statement writeTimeStatement = null;
        String writeTimeQuery = null;
        if (state.equals("IN")) {
            writeTimeQuery = "INSERT INTO Timerecords (name, intime) VALUES ('" + name + "', GETDATE())";
        } else {
            writeTimeQuery = "INSERT INTO Timerecords (name, outTime) VALUES ('" + name + "', NULL, GETDATE())";
        }

        try {
            System.out.println(writeTimeQuery);
            writeTimeStatement = connection.createStatement();
            writeTimeStatement.executeUpdate(writeTimeQuery);
        } catch (SQLException s) {
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
        return writeTimeQuery;
    }

    public void createCompleteRecord(String name, String inTime, String outTime, String note) {
        PreparedStatement writeTimeStatement = null;
        String writeTimeQuery = null;

        writeTimeQuery = "INSERT INTO Timerecords (name, inTime, outTime, Notes) VALUES (?,?,?,?)";

        try {
            System.out.println(writeTimeQuery);
            writeTimeStatement = connection.prepareStatement(writeTimeQuery);

            writeTimeStatement.setString(1, name);
            writeTimeStatement.setString(2, inTime);
            writeTimeStatement.setString(3, outTime);
            writeTimeStatement.setString(4, note);

            writeTimeStatement.executeUpdate();
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            if (writeTimeStatement != null) {
                try {
                    writeTimeStatement.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            }
        }
    }

    public String outTime(String name, String timeMil, String state) {
        Statement writeTimeStatement = null;
        //String writeOutQuery = "INSERT INTO Timerecords (name, time, state) VALUES ('" + name + "','" + timeMil + "','" + state + "')";
        String writeOutQuery = "UPDATE Timerecords SET outTime = GETDATE() WHERE name = '" + name + "' AND outTime IS NULL AND inTime > CONVERT(date, GetDate())";


        try {
            //query for record with no out time
            System.out.println(writeOutQuery);
            writeTimeStatement = connection.createStatement();
            writeTimeStatement.executeUpdate(writeOutQuery);
        } catch (SQLException s) {
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
        return writeOutQuery;
    }

    public void updateTime(String rowid, String inTime, String outTime, String note) {
        Statement writeTimeStatement = null;
        String updateTimeQuery;
        if (outTime.equals("NULL")) {
            updateTimeQuery = "UPDATE Timerecords SET outTime = " + outTime + ", inTime = '" + inTime + "', Notes = '" + note + "' WHERE recordId = '" + rowid + "'";
        } else {
            updateTimeQuery = "UPDATE Timerecords SET outTime = '" + outTime + "', inTime = '" + inTime + "', Notes = '" + note + "' WHERE recordId = '" + rowid + "'";
        }

        try {
            System.out.println("Updating row query: " + updateTimeQuery);
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

    public void massUpdateTime(TableModel timeModel) {

        Statement statement = null;

        try {
            statement = connection.createStatement();

            int numberOfRows = timeModel.getRowCount();
            System.out.println(numberOfRows);
            for (int i = 0; i < numberOfRows; i++) {
                String rowId = timeModel.getValueAt(i, 0).toString();

                Object inTime = timeModel.getValueAt(i, 2);
                Object outTime = timeModel.getValueAt(i, 3);
                if (inTime != null && outTime != null) {
                    try {
                        inTime = TimeFunctions.convertTimestamp(inTime.toString());
                        outTime = TimeFunctions.convertTimestamp(outTime.toString());
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                } else {
                    outTime = "NULL";
                }

                Object note = timeModel.getValueAt(i, 5);
                if (note != null) {
                    note = note.toString();
                } else {
                    note = "";
                }

                //System.out.println("RowID: " + rowId + " inTime: " + inTime + " outTime: " + outTime + "Note: " + note);

                if (outTime.equals("NULL")) {
                    statement.addBatch("UPDATE Timerecords SET outTime = " + outTime + ", inTime = '" + inTime + "', Notes = '" + note + "' WHERE recordId = '" + rowId + "'");
                } else {
                    statement.addBatch("UPDATE Timerecords SET outTime = '" + outTime + "', inTime = '" + inTime + "', Notes = '" + note + "' WHERE recordId = '" + rowId + "'");
                }
            }

            int[] numRecordsAffected = statement.executeBatch();
            System.out.println("Number of records affected: " + numRecordsAffected.length);

        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    public void deleteRecord(String rowid) {
        Statement deleteTimeStatement = null;
        String deleteRecordQuery = "delete FROM Timerecords WHERE recordId = '" + rowid + "'";


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

    public void addEmployee(String name, String pin, int manager, String email) {
        Statement writeTimeStatement = null;
        String writeTimeQuery = "INSERT INTO Employees (name, pin, manager, email) VALUES ('" + name + "', '" + pin + "', '" + manager + "', '" + email + "')";

        try {
            System.out.println(writeTimeQuery);
            writeTimeStatement = connection.createStatement();
            writeTimeStatement.executeUpdate(writeTimeQuery);
        } catch (SQLException s) {
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

    public void updateEmployeeRecord(String rowid, String name, String pin, int manager, String email) {
        Statement writeTimeStatement = null;
        String updateTimeQuery = "update Employees set Name = '" + name + "', pin = '" + pin + "', Manager = '" + manager + "', email = '" + email + "' where employeeId = '" + rowid + "'";

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

    public void deleteEmployeeRecord(String rowid) {
        Statement deleteTimeStatement = null;
        String deleteRecordQuery = "delete from Employees where employeeId = '" + rowid + "'";


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

    public DefaultTableModel getEmployeeTimeModel(String inTime, String outTime) {
        String getEmployeeTime = "SELECT recordId, name, inTime, outTime, CONVERT(VARCHAR,(outTime-inTime),108) TotalTime, Notes FROM Timerecords WHERE CONVERT(date,inTime) BETWEEN '" + inTime + "' AND '" + outTime + "' or outTime IS null or inTime IS null ORDER BY inTime DESC";

        DefaultTableModel timeTable = buildModel(getEmployeeTime);
        int rowCount = timeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            //Load timestamps to string
            String originalIn = null;
            String originalOut = null;

            //set converted values
            try {
                if (timeTable.getValueAt(i, 2) != null) {
                    originalIn = timeTable.getValueAt(i, 2).toString();
                    timeTable.setValueAt(TimeFunctions.convertTimestamp(originalIn), i, 2);
                }

                if (timeTable.getValueAt(i, 3) != null) {
                    originalOut = timeTable.getValueAt(i, 3).toString();
                    timeTable.setValueAt(TimeFunctions.convertTimestamp(originalOut), i, 3);
                }
            } catch (ParseException p) {
                p.printStackTrace();
            }
        }
        //Return modified table
        return timeTable;
    }

    public DefaultTableModel getSpecificEmployeeModel(String name, String inTime, String outTime) {
        String getEmployeeSpecificTime = "SELECT recordId, name, inTime, outTime, CONVERT(VARCHAR,(outTime-inTime),108) TotalTime FROM Timerecords WHERE CONVERT(date,inTime) BETWEEN '" + inTime + "' AND '" + outTime + "' OR outTime IS NULL OR inTime IS NULL AND name = '" + name + "' ORDER BY outTime DESC";

        return buildModel(getEmployeeSpecificTime);
    }

    public DefaultTableModel builEmployeesTableModel() {
        String getEmployeeTable = "SELECT employeeid, Name, pin, Manager, email  FROM Employees";
        return buildModel(getEmployeeTable);
    }

    public DefaultTableModel buildEmailModel() {
        String getAllTimeQuery = "SELECT Name, email FROM Employees";
        return buildModel(getAllTimeQuery);
    }

    public DefaultTableModel buildModel(String query) {
        Statement getTime;

        try {
            getTime = connection.createStatement();
            System.out.println("Running: " + query);
            ResultSet rs = getTime.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // data of the table
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
            return new DefaultTableModel(data, columnNames);
        } catch (SQLException s) {
            s.printStackTrace();
            return null;
        }
    }

    public DefaultTableModel connectionTestQuery() {
        return buildModel("select 1 from Timerecords");

    }
}
