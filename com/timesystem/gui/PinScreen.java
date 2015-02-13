package com.timesystem.gui;

import com.timesystem.cont.PinController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PinScreen {
    private static String version = "1.5.0";

    //Load Connector
    PinController pinController = new PinController(this);

    private EmployeePane employeePane;
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
    private JLabel versionLabel;
    private JPanel timeManager;


    /**
     * Main UI
     */
    public PinScreen() {

        pinController.loadServerSettings();
        frame = new JFrame("OpenTimeSystem");
        frame.setContentPane(pinPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(960, 540);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        versionLabel.setText("Version:" + version);

        INButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.clockIn(pinField.getPassword());
            }
        });

        OUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.clockOut(pinField.getPassword());
            }
        });

        //Handle admin login button
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.adminLogin();
            }
        });

        adminLoginPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.adminLogin();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (adminTab.getTabCount() == 5) {
                    adminTab.remove(timeManager);
                } else {
                    adminTab.remove(4);
                    adminTab.remove(4);
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
                pinController.connectDatabase();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.saveSettings();
            }
        });
        compSetSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pinController.saveCompanyName();
            }
        });

        pinField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Check for I and O keys for clock in/out
                if (e.getKeyChar() == 'i') {
                    pinController.clockIn(pinField.getPassword());
                } else if (e.getKeyChar() == 'o') {
                    pinController.clockOut(pinField.getPassword());
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

    public String getCompName() {
        return compNameText.getText();
    }

    public JTextField getServerField() {
        return serverField;
    }

    public JTextField getPortField() {
        return portField;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getDatabaseField() {
        return databaseField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JPasswordField getPinField() {
        return pinField;
    }

    public JLabel getNameTime() {
        return nameTime;
    }

    public String getAdminLoginUsername() {
        return adminLoginUsername.getText();
    }

    public String getAdminLoginPassword() {
        return String.valueOf(adminLoginPassword.getPassword());
    }

    public JTabbedPane getAdminTab() {
        return adminTab;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
