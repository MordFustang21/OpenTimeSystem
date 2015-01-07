package com.timesystem.gui;

import com.timesystem.cont.TimeFunctions;
import com.timesystem.gui.Panels.adminPane;
import com.timesystem.mod.Databasedat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Created by dlaird on 12/13/14.
 */
public class NewRecordCreator extends JFrame {
    private JTextField nameField;
    private JTextField startTimeField;
    private JTextField stopTimeField;
    private JTextField noteField;
    private JButton createButton;
    private JButton cancelButton;
    private JPanel recordCreatorPanel;

    private String name = null;
    private String inTime = null;
    private String outTime = null;
    private String note = null;


    public NewRecordCreator(final adminPane adminPane) {
        setTitle("Create Record");
        setContentPane(recordCreatorPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 225);
        setLocationRelativeTo(null);
        setVisible(true);

        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());

        startTimeField.setText(TimeFunctions.convertTimestamp(timestamp));
        stopTimeField.setText(TimeFunctions.convertTimestamp(timestamp));


        //Action listeners
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();

                //Try and parse times into timestamps
                try {
                    inTime = TimeFunctions.getTimestamp(startTimeField.getText());
                    outTime = TimeFunctions.getTimestamp(stopTimeField.getText());
                } catch (ParseException p) {
                    p.printStackTrace();
                }

                note = noteField.getText();

                Databasedat.getInstance().createCompleteRecord(name, inTime, outTime, note);

                adminPane.reloadTableModel();

                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
