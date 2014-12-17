package com.timesystem.gui;

import com.timesystem.EmployeeData;
import com.timesystem.mod.Databasedat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class EmployeePane {


    private JTextField employeeName;
    private JTextField employeePin;
    private JButton addEmployeeButton;
    private JPanel employeePanel;
    private Databasedat databasedat = Databasedat.getInstance();
    private EmployeeData employeeData;

    //Table Data
    private JTable employeeTable;
    private JCheckBox managerCheckBox;
    private JTextField employeeEmail;
    private JPopupMenu tableMenu;


    public EmployeePane(Connection connection) {
        employeeData = new EmployeeData(connection);

        employeeTable.setModel(databasedat.buildEmployeeTableModel());

        tableMenu = new JPopupMenu();
        employeeTable.setComponentPopupMenu(tableMenu);
        addMenuItems();
        loadClickListeners();
    }

    public void loadClickListeners() {
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int manager = 0;
                if (managerCheckBox.isSelected()) {
                    databasedat.addEmployee(employeeName.getText(), employeePin.getText(), 1, employeeEmail.getText());
                } else {
                    databasedat.addEmployee(employeeName.getText(), employeePin.getText(), 0, employeeEmail.getText());
                }
                employeeName.setText("");
                employeePin.setText("");
                employeeEmail.setText("");
                managerCheckBox.setSelected(false);
                reloadTableModel();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        employeePanel = new JPanel();
    }

    public void addMenuItems() {
        JMenuItem delete = new JMenuItem("Delete");
        tableMenu.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRow();
            }
        });

        JMenuItem update = new JMenuItem("Update");
        tableMenu.add(update);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedRow();
            }
        });

        JMenuItem updateAllMenu = new JMenuItem("Update All");
        tableMenu.add(updateAllMenu);
        updateAllMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAll();
            }
        });

    }

    public void updateSelectedRow() {
        int row = employeeTable.getSelectedRow();
        String rowId = employeeTable.getValueAt(row, 0).toString();
        String name = employeeTable.getValueAt(row, 1).toString();
        String pin = employeeTable.getValueAt(row, 2).toString();
        int manager = Integer.parseInt(employeeTable.getValueAt(row, 3).toString());
        Object email = employeeTable.getValueAt(row, 4);

        if (email != null) {
            email = email.toString();
        } else {
            email = "";
        }

        employeeData.updateEmployeeRecord(rowId, name, pin, manager, email.toString());
        System.out.println("RowID: " + rowId + " Name: " + name + " Pin: " + pin);
    }

    public void updateAll() {
        int numberOfRows = employeeTable.getModel().getRowCount();
        System.out.println(numberOfRows);
        for (int i = 0; i < numberOfRows; i++) {
            String rowId = employeeTable.getValueAt(i, 0).toString();
            String name = employeeTable.getValueAt(i, 1).toString();
            String pin = employeeTable.getValueAt(i, 2).toString();
            int manager = Integer.parseInt(employeeTable.getValueAt(i, 3).toString());
            Object email = employeeTable.getValueAt(i, 4);

            if (email != null) {
                email = email.toString();
            } else {
                email = "";
            }

            employeeData.updateEmployeeRecord(rowId, name, pin, manager, email.toString());
            System.out.println("RowID: " + rowId + " Name: " + name + " Pin: " + pin);
        }
    }

    public void deleteRow() {
        int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected record?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            System.out.println("Aborted");
        } else if (response == JOptionPane.YES_OPTION) {
            System.out.println("Yes button clicked");
            int row = employeeTable.getSelectedRow();
            System.out.println("Deleting Record: " + employeeTable.getValueAt(row, 0));
            employeeData.deleteRecord(employeeTable.getValueAt(row, 0).toString());
            reloadTableModel();
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
    }

    public void reloadTableModel() {
        employeeTable.setModel(databasedat.buildEmployeeTableModel());
    }

    public JPanel getPanel() {
        return employeePanel;
    }
}
