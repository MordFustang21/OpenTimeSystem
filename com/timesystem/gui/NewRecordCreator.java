package com.timesystem.gui;

import com.timesystem.gui.Panels.adminPane;
import com.timesystem.mod.Databasedat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Timestamp;
import java.util.Date;

/**
 * Created by dlaird on 12/13/14.
 */
public class NewRecordCreator {
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
        final JFrame frame = new JFrame("NewRecordCreator");
        frame.setContentPane(recordCreatorPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(300, 225);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String timestamp = new java.sql.Timestamp(System.currentTimeMillis()).toString();
        startTimeField.setText(timestamp);
        stopTimeField.setText(timestamp);


        //Action listeners
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();
                inTime = startTimeField.getText();
                outTime = stopTimeField.getText();
                note = noteField.getText();

                Databasedat.getInstance().createCompleteRecord(name,inTime,outTime,note);

                adminPane.reloadTableModel();

                frame.dispose();
            }
        });

    }
}
