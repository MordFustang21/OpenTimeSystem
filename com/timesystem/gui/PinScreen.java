package com.timesystem.gui;

import com.timesystem.DatabaseConnector;
import com.timesystem.gui.Panels.adminPane;
import com.timesystem.mod.Databasedat;
import com.timesystem.mod.ServerProp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PinScreen {
    private static String version = "1.0.1";

    //Load Connector
    DatabaseConnector databaseConnector = new DatabaseConnector();
    Databasedat databasedat = Databasedat.getInstance();
    ServerProp serverProp = new ServerProp();
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    private EmployeePane employeePane;
    private Connection connection;
    private JFrame frame;
    private JPanel pinPanel;
    private JTabbedPane adminTab;
    private JPasswordField pinField;
    private JTextField serverField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField adminLoginUsername;
    private JPasswordField adminLoginPassword;
    private JButton adminLoginButton;
    private JButton saveButton;
    private JButton connectButton;
    private JButton OUTButton;
    private JButton INButton;
    private JLabel nameTime;
    private JPanel companySetting;
    private JTextField compNameText;
    private JButton compSetSave;
    private JButton logoutButton;
    private JTextField databaseField;
    private JCheckBox mySqlCheckBox;
    private JLabel versionLabel;
    private JPanel timeManager;

    public PinScreen() {

        loadServerSettings();
        this.frame = new JFrame("OpenTimeSystem");
        frame.setContentPane(pinPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(960, 540);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        versionLabel.setText("Version:" + version);

        INButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockIn();
            }
        });

        OUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockOut();
            }
        });

        //Handle admin login button
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        adminLoginPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (adminTab.getTabCount() == 5) {
                    adminTab.remove(timeManager);
                } else {
                    adminTab.remove(timeManager);
                    adminTab.remove(employeePane.getPanel());
                }
                adminLoginPassword.setText("");
                adminLoginPassword.requestFocus();
                logoutButton.setVisible(false);
            }
        });


        //Server Settings
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mySqlCheckBox.isSelected()) {
                    connection = databaseConnector.makeMysqlConnection(serverField.getText(), portField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword()), databaseField.getText());
                } else {
                    connection = databaseConnector.makeConnection(serverField.getText(), portField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword()), databaseField.getText());
                }
                databasedat.setConnection(connection);
                connectButton.setText("Connected");
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverProp.saveProperties(serverField.getText(), portField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword()), databaseField.getText());
            }
        });
        compSetSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverProp.saveCompanyName(compNameText.getText());
            }
        });

        pinField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'i') {
                    clockIn();
                } else if (e.getKeyChar() == 'o') {
                    clockOut();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    public void clockIn() {
        if (checkConnection()) {
            String employeeName = databasedat.getName(String.valueOf(pinField.getPassword()));

            if (employeeName.equals("Employee doesn't exist")) {
                JOptionPane.showMessageDialog(frame, "Employee doesn't exist");
                pinField.requestFocus();
            } else {
                Timestamp inDate = new Timestamp(System.currentTimeMillis());
                System.out.println(employeeName + " IN @ " + sdf.format(System.currentTimeMillis()));

                nameTime.setText(employeeName + " IN @ " + sdf.format(inDate));
                databasedat.createRecord(employeeName, "IN");

                afterPinCommands();
            }
        }
    }

    public void clockOut() {
        if (checkConnection()) {
            String employeeName = databasedat.getName(String.valueOf(pinField.getPassword()));

            //If no in then create out
            if (employeeName.equals("Employee doesn't exist")) {
                JOptionPane.showMessageDialog(frame, "Employee doesn't exist");
                pinField.requestFocus();
            } else {
                if (databasedat.checkForIn(employeeName) == 0) {
                    Timestamp outDate = new Timestamp(System.currentTimeMillis());
                    System.out.println(employeeName + " OUT @ " + outDate);

                    nameTime.setText(employeeName + " OUT @ " + sdf.format(outDate));

                    //write info to db
                    databasedat.createRecord(employeeName, "OUT");

                    //Timer for the text and pin
                    afterPinCommands();
                } else {

                    Timestamp outDate = new Timestamp(System.currentTimeMillis());
                    System.out.println(employeeName + " OUT @ " + outDate);

                    nameTime.setText(employeeName + " OUT @ " + sdf.format(outDate));

                    //write info to db
                    databasedat.outTime(employeeName, "" + outDate, "OUT");

                    //Timer for the text and pin
                    afterPinCommands();
                }
            }
        }
    }

    public void adminLogin() {
        //Check credentials
        int userType = databasedat.getAdminCredentials(adminLoginUsername.getText(), String.valueOf(adminLoginPassword.getPassword()));
        if (userType == 0) {
            System.out.println("Failed login for " + adminLoginUsername.getText());
            JOptionPane.showMessageDialog(frame, "Invalid Username or password");
        } else if (userType == 1) {
            System.out.println("Non Manager login");
            if (adminTab.getTabCount() == 4) {
                timeManager = new adminPane(1, adminLoginUsername.getText());
                adminTab.add("Time Managment", timeManager);
                adminTab.setSelectedIndex(4);
                logoutButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Already Logged In");
            }

        } else if (userType == 2) {
            System.out.println("Manager login");
            if (adminTab.getTabCount() == 4) {
                //Handle timeManger
                timeManager = new adminPane(userType, adminLoginUsername.getText());
                adminTab.add("Time Managment", timeManager);
                adminTab.setSelectedIndex(4);

                //Handle EmployeeManager
                employeePane = new EmployeePane(connection);
                adminTab.add("Employee Management", employeePane.getPanel());
                logoutButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Already Logged In");
            }
        }
    }

    public void loadServerSettings() {
        serverField.setText(serverProp.getServer());
        portField.setText(serverProp.getPort());
        usernameField.setText(serverProp.getUsername());
        passwordField.setText(serverProp.getPassword());
        databaseField.setText(serverProp.getDatabase());

        compNameText.setText(serverProp.getCompName());
        System.out.println("Settings load successful");
    }

    public void afterPinCommands() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        nameTime.setText(serverProp.getCompName());
                        pinField.setText("");
                        pinField.requestFocus();
                    }

                },
                3000
        );

        pinField.requestFocus();
    }

    public boolean checkConnection() {
        try {
            if (connection.isClosed()) {
                JOptionPane.showMessageDialog(frame, "Please reconnect");
                return false;
            } else {
                return true;
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return false;
    }

}
