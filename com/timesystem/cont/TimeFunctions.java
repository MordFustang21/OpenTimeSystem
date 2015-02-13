package com.timesystem.cont;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dlaird on 10/30/14.
 */
public class TimeFunctions {

    /**
     * Method used to calculate total time from JTable
     *
     * @param selected          If true use selected else use whole table
     * @param employeeTimeTable Table used for data
     * @return Total time returned
     */

    public static String calculateTotalHours(boolean selected, JTable employeeTimeTable) {
        TableModel tableModel = employeeTimeTable.getModel();
        int totalHours = 0;
        int totalMinutes = 0;
        int totalSeconds = 0;
        if (selected) {
            int[] rowlist = employeeTimeTable.getSelectedRows();
            for (int i = 0; i < rowlist.length; i++) {

                //Convert timeValue
                Object timeValue = tableModel.getValueAt(rowlist[i], 4);
                if (timeValue != null) {
                    timeValue = timeValue.toString();
                } else {
                    timeValue = "00:00:00";
                }

                String[] h1 = timeValue.toString().split(":");

                int hour = Integer.parseInt(h1[0]);
                int minute = Integer.parseInt(h1[1]);
                int second = Integer.parseInt(h1[2]);

                totalHours = totalHours + hour;
                totalMinutes = totalMinutes + minute;
                totalSeconds = totalSeconds + second;

            }
        } else {
            int numberOfRows = tableModel.getRowCount();
            for (int i = 0; i < numberOfRows; i++) {
                Object h = tableModel.getValueAt(i, 4);
                if (h == null || h.toString().isEmpty()) {
                    h = "00:00:00";
                } else {
                    h = h.toString();
                }

                String[] h1 = h.toString().split(":");

                int hour = Integer.parseInt(h1[0]);
                int minute = Integer.parseInt(h1[1]);
                int second = Integer.parseInt(h1[2]);

                totalHours = totalHours + hour;
                totalMinutes = totalMinutes + minute;
                totalSeconds = totalSeconds + second;
            }
        }

        //Add seconds to minutes
        totalMinutes = totalMinutes + (totalSeconds / 60);
        totalSeconds = totalSeconds - ((totalSeconds / 60) * 60);

        //add minutes to hours
        totalHours = totalHours + (totalMinutes / 60);
        totalMinutes = totalMinutes - ((totalMinutes / 60) * 60);

        System.out.println("Hours:" + totalHours + " Minutes:" + totalMinutes + " Seconds:" + totalSeconds);
        return "TotalTime: " + totalHours + ":" + totalMinutes + ":" + totalSeconds;
    }

    public static String calculateTotalHours(String[][] time) {
        int totalHours = 0;
        int totalMinutes = 0;
        int totalSeconds = 0;

        for (int i = 0; i < time.length; i++) {

            //Convert timeValue
            String timeValue = time[i][3];
            if (timeValue.equals("Incomplete")) {
                timeValue = "00:00:00";
            }

            String[] h1 = timeValue.split(":");

            int hour = Integer.parseInt(h1[0]);
            int minute = Integer.parseInt(h1[1]);
            int second = Integer.parseInt(h1[2]);

            totalHours = totalHours + hour;
            totalMinutes = totalMinutes + minute;
            totalSeconds = totalSeconds + second;
        }

        //Add seconds to minutes
        totalMinutes = totalMinutes + (totalSeconds / 60);
        totalSeconds = totalSeconds - ((totalSeconds / 60) * 60);

        //add minutes to hours
        totalHours = totalHours + (totalMinutes / 60);
        totalMinutes = totalMinutes - ((totalMinutes / 60) * 60);

        System.out.println("Hours:" + totalHours + " Minutes:" + totalMinutes + " Seconds:" + totalSeconds);
        return "TotalTime: " + totalHours + ":" + totalMinutes + ":" + totalSeconds;
    }


    /**
     * Converts timestamp to hour format
     *
     * @param inStamp timestamp to be converted
     * @return converted timestamp
     */
    public static String convertTimestamp(Timestamp inStamp) {
        String convertedValue = null;

        //Convert instamp to am/pm
        Date date = new Date(inStamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");

        convertedValue = simpleDateFormat.format(date);

        return convertedValue;
    }

    public static String convertTimestamp(String inStamp) throws ParseException {

        //Convert hourFormat
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        Date date = simpleDateFormat.parse(inStamp);

        Timestamp timestamp = new Timestamp(date.getTime());

        return convertTimestamp(timestamp);
    }

    /**
     * Converts string to timestamp string
     *
     * @param hourFormat time string to be converted
     * @return converted time string
     * @throws ParseException
     */
    public static String getTimestamp(String hourFormat) throws ParseException {

        //Convert hourFormat
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
        Date date = simpleDateFormat.parse(hourFormat);

        Timestamp timestamp = new Timestamp(date.getTime());

        String convertedValue = timestamp.toString();

        return convertedValue;
    }

    public static DefaultTableModel convertTimeTable(DefaultTableModel tableModel){

        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            //Load timestamps to string
            String originalIn = null;
            String originalOut = null;

            //set converted values
            try {
                if (tableModel.getValueAt(i, 2) != null) {
                    originalIn = tableModel.getValueAt(i, 2).toString();
                    tableModel.setValueAt(TimeFunctions.convertTimestamp(originalIn), i, 2);
                }

                if (tableModel.getValueAt(i, 3) != null) {
                    originalOut = tableModel.getValueAt(i, 3).toString();
                    tableModel.setValueAt(TimeFunctions.convertTimestamp(originalOut), i, 3);
                }
            } catch (ParseException p) {
                p.printStackTrace();
            }
        }

        return tableModel;
    }
}
