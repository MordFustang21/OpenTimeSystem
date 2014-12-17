package com.timesystem.gui.Panels;

import com.timesystem.misc.SendEmail;
import com.timesystem.mod.Databasedat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EmailSelector extends JFrame {

    public EmailSelector(String message) {

        setTitle("Export Preview");
        setContentPane(new EmailSelectorPane(this, message));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class EmailSelectorPane extends JPanel {
    private String emailAddress;
    private String message;

    public EmailSelectorPane(final JFrame frame, String message) {
        this.setLayout(new BorderLayout());
        this.message = message;

        Databasedat databasedat = Databasedat.getInstance();

        DefaultTableModel emailTableModel = databasedat.buildEmailModel();

        final JTable emailListTable = new JTable(emailTableModel);
        JScrollPane jScrollPane = new JScrollPane(emailListTable);
        this.add(jScrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        JButton useEmail = new JButton("Send");
        useEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailAddress = emailListTable.getValueAt(emailListTable.getSelectedRow(), 1).toString();
                frame.dispose();
                sendEmail();

            }
        });
        southPanel.add(close);
        southPanel.add(useEmail);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    public void sendEmail() {
        new SendEmail(message, emailAddress);
    }
}

