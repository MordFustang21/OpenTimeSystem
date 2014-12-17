package com.timesystem.gui.Panels;

import com.timesystem.misc.SendEmail;
import com.timesystem.misc.TimeExport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dlaird on 11/24/14.
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
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Begin day check
        Date endDay = null;
        try {
            endDay = dayFormat.parse(time[0][1]);
        } catch (ParseException p) {
            p.printStackTrace();
        }
        String endDayFormatted = dayFormat.format(endDay);
        String[] splitDateEnd = endDayFormatted.split("-");
        int endDayNum = Integer.parseInt(splitDateEnd[2]);
        System.out.println("End Date: " + endDayNum);
        //End Begin day check
        timePreview.append("Name: " + time[0][0] + "\n\n");

        timePreview.append("InTime \t\t OutTime \t\t TotalTime\n");

        for (int i = 0; i < time.length; i++) {

            Date inDate = null;
            Date outDate = null;
            try {
                //System.out.println(time[i][1]);
                //System.out.println(time[i][2]);
                inDate = timeFormat.parse(time[i][1]);
                outDate = timeFormat.parse(time[i][2]);
            } catch (ParseException p) {
                p.printStackTrace();
            }

            //Create formatted dates
            String inDateFormatted = timeFormat.format(inDate);
            String outDateFormatted = null;
            //If value cant be parsed its null
            if (outDate == null) {
                outDateFormatted = "-------Incomplete------ ";
            } else {
                outDateFormatted = timeFormat.format(outDate);
            }

            timePreview.append(inDateFormatted + "    " + outDateFormatted + "   \t" + time[i][3] + "\n");

        }

        timePreview.append("\n" + adminPane.timeFunctions.calculateTotalHours(true) + "\n\n\n\n");
        timePreview.append("Sent using OpenTimeSystem");

    }
}
