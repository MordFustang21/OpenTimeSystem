package com.timesystem.cont;

import com.timesystem.gui.EmployeePane;
import com.timesystem.gui.Panels.adminPane;
import com.timesystem.gui.PinScreen;
import com.timesystem.mod.ConnectionFactory;
import com.timesystem.mod.Databasedat;
import com.timesystem.mod.ServerProp;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Handles the controlling of the pin screen
 */

public class PinController {
    private PinScreen pinScreen;
    private Databasedat databasedat = Databasedat.getInstance();
    private ServerProp serverProp = new ServerProp();
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

    public PinController(PinScreen pinScreen) {
        this.pinScreen = pinScreen;

    }

    public void connectDatabase() {
        String server = pinScreen.getServerField().getText();
        String port = pinScreen.getPortField().getText();
        String database = pinScreen.getDatabaseField().getText();
        String username = pinScreen.getUsernameField().getText();
        String password = String.valueOf(pinScreen.getPasswordField().getPassword());

        databasedat.setConnection(ConnectionFactory.getMSSQLConnection(server, port, username, password, database));
    }

    public void saveSettings() {
        String server = pinScreen.getServerField().getText();
        String port = pinScreen.getPortField().getText();
        String database = pinScreen.getDatabaseField().getText();
        String username = pinScreen.getUsernameField().getText();
        String password = String.valueOf(pinScreen.getPasswordField().getPassword());
        serverProp.saveProperties(server, port, username, password, database);
    }

    public void saveCompanyName() {
        serverProp.saveCompanyName(pinScreen.getCompName());
    }

    public void clockIn(char[] pin) {
        if (testConnection()) {
            String employeeName = databasedat.getName(String.valueOf(pin));

            if (employeeName.equals("Employee doesn't exist")) {
                JOptionPane.showMessageDialog(new JFrame(), "Employee doesn't exist");
                pinScreen.getPasswordField().requestFocus();
            } else {
                Timestamp inDate = new Timestamp(System.currentTimeMillis());
                pinScreen.getNameTime().setText(employeeName + " IN @ " + sdf.format(inDate));
                databasedat.createRecord(employeeName, "IN");
                afterPinCommands();
            }
        }
    }

    public void clockOut(char[] pin) {
        if (testConnection()) {
            String employeeName = databasedat.getName(String.valueOf(pin));

            //If no in then create out
            if (employeeName.equals("Employee doesn't exist")) {
                alertNoEmployee();
            } else {
                if (databasedat.checkForIn(employeeName) == 0) {
                    Timestamp outDate = new Timestamp(System.currentTimeMillis());
                    System.out.println(employeeName + " OUT @ " + outDate);

                    pinScreen.getNameTime().setText(employeeName + " OUT @ " + sdf.format(outDate));

                    //write info to db
                    databasedat.createRecord(employeeName, "OUT");

                    //Timer for the text and pin
                    afterPinCommands();
                } else {
                    Timestamp outDate = new Timestamp(System.currentTimeMillis());
                    System.out.println(employeeName + " OUT @ " + outDate);

                    pinScreen.getNameTime().setText(employeeName + " OUT @ " + sdf.format(outDate));

                    //write info to db
                    databasedat.outTime(employeeName, "" + outDate, "OUT");

                    //Timer for the text and pin
                    afterPinCommands();
                }
            }
        }
    }

    public int getAdminCredentials(String username, String password) {
        return databasedat.getAdminCredentials(username, password);
    }

    public void alertNoEmployee() {
        JOptionPane.showMessageDialog(new JFrame(), "Employee doesn't exist");
        pinScreen.getPinField().requestFocus();
    }

    public String getTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public void loadServerSettings() {
        pinScreen.getServerField().setText(serverProp.getServer());
        pinScreen.getPortField().setText(serverProp.getPort());
        pinScreen.getUsernameField().setText(serverProp.getUsername());
        pinScreen.getPasswordField().setText(serverProp.getPassword());
        pinScreen.getDatabaseField().setText(serverProp.getDatabase());
    }


    public void afterPinCommands() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        pinScreen.getNameTime().setText(serverProp.getCompName());
                        pinScreen.getPinField().setText("");
                        pinScreen.getPinField().requestFocus();
                    }

                },
                3000
        );

        pinScreen.getPinField().requestFocus();
    }


    public void adminLogin() {
        //Check credentials
        String adminUsername = pinScreen.getAdminLoginUsername();
        String password = pinScreen.getAdminLoginPassword();

        //Tab Count
        int tabCount = pinScreen.getAdminTab().getTabCount();

        //Admin Tab
        JTabbedPane adminTab = pinScreen.getAdminTab();

        //Get usertype
        int userType = getAdminCredentials(adminUsername, password);


        if (userType == 0) {
            System.out.println("Failed login for " + adminUsername);
            JOptionPane.showMessageDialog(null, "Invalid Username or password");
        } else if (userType == 1) {
            System.out.println("Non Manager login");
            if (tabCount == 4) {
                adminTab.add("Time Managment", new adminPane(1, adminUsername));
                adminTab.setSelectedIndex(4);
                adminTab.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Already Logged In");
            }

        } else if (userType == 2) {
            System.out.println("Manager login");

            if (tabCount == 4) {
                //Handle timeManger
                adminTab.add("Time Managment", new adminPane(userType, adminUsername));
                adminTab.setSelectedIndex(4);

                //Handle EmployeeManager
                adminTab.add("Employee Management", new EmployeePane().getPanel());
                pinScreen.getLogoutButton().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Already Logged In");
            }
        }
    }


    public boolean testConnection() {
        databasedat.connectionTestQuery();
        try {
            if (databasedat.getConnection().isClosed()) {
                JOptionPane.showMessageDialog(null, "Connection is closed please reconnect");
                return false;
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return true;
    }

}
