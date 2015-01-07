package com.timesystem.gui.Panels;

import com.timesystem.cont.TimeFunctions;
import com.timesystem.misc.TimeExport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Export pane for exporting data to text file/email
 */
public class TimeExportPane extends JPanel {

    private JTextArea timePreview;
    private JPanel exportOptions;
    private adminPane adminPane;
    private TimeExport timeExport;

    private String[][] time;

    public TimeExportPane(adminPane adminPane, TimeExport timeExport) {
        this.adminPane = adminPane;
        this.timeExport = timeExport;

        time = adminPane.buildExport();

        setLayout(new BorderLayout());

        //Add components to panel
        addComponents();
        showTime();
    }

    public void addComponents() {
        timePreview = new JTextArea();
        this.add(timePreview, BorderLayout.CENTER);

        //Export Options
        exportOptions = new JPanel();
        this.add(exportOptions, BorderLayout.SOUTH);
        JButton cancel = new JButton("Cancel");
        exportOptions.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeExport.dispose();
            }
        });

        JButton email = new JButton("Email Timecard");
        exportOptions.add(email);
        email.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmailList();
            }
        });
        JButton save = new JButton("Save");
        exportOptions.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    //Shows the email list
    public void showEmailList() {
        new EmailSelector(timePreview.getText());
    }

    public void showTime() {
        timePreview.append("Name: " + time[0][0] + "\n\n");

        timePreview.append("InTime \t\t OutTime \t\t TotalTime\n");

        for (int i = 0; i < time.length; i++) {

            String inTime = time[i][1];
            String outTime = time[i][2];

            timePreview.append(inTime + "    " + outTime + "   \t" + time[i][3] + "\n");
        }

        timePreview.append("\n" + TimeFunctions.calculateTotalHours(time) + "\n\n\n\n");
        timePreview.append("Sent using OpenTimeSystem");

    }
}
