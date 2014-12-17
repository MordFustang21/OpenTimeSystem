package com.timesystem.cont;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * Created by dlaird on 10/30/14.
 */
public class TimeFunctions {

    JTable employeeTimeTable;
    TableModel tableModel;

    public void setJTable(JTable inTable) {
        employeeTimeTable = inTable;
    }

    public void setTableModel(TableModel inTableModel) {
        tableModel = inTableModel;
    }

    public String calculateTotalHours(boolean selected) {
        int totalHours = 0;
        int totalMinutes = 0;
        int totalSeconds = 0;
        if (selected) {
            int[] rowlist = employeeTimeTable.getSelectedRows();
            for (int i = 0; i < rowlist.length; i++) {
                Object h = tableModel.getValueAt(rowlist[i], 4);
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


        System.out.println(totalSeconds / 60);
        System.out.println(totalMinutes / 60);
        //Add seconds to minutes
        totalMinutes = totalMinutes + (totalSeconds / 60);
        totalSeconds = totalSeconds - ((totalSeconds / 60) * 60);

        //add minutes to hours
        totalHours = totalHours + (totalMinutes / 60);
        totalMinutes = totalMinutes - ((totalMinutes / 60) * 60);

        System.out.println("Hours:" + totalHours + " Minutes:" + totalMinutes + " Seconds:" + totalSeconds);
        return "TotalTime: " + totalHours + ":" + totalMinutes + ":" + totalSeconds;
    }
}
