package com.timesystem.gui.Panels;

import com.timesystem.cont.TimeFunctions;
import com.timesystem.gui.NewRecordCreator;
import com.timesystem.misc.TimeExport;
import com.timesystem.mod.Databasedat;
import com.timesystem.mod.ServerProp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminPane extends JPanel {
    private adminPane adminPane = this;
    private JComboBox<String> employeeList;
    private DefaultComboBoxModel<String> employeeModel = new DefaultComboBoxModel<String>();
    private Databasedat databasedat;
    private JLabel totalHoursLabel;
    private JTable employeeTimeTable;
    private int loginType;
    private String user;
    private JTextField dateStart;
    private JTextField dateStop;
    private ServerProp serverProp = new ServerProp();
    public TimeFunctions timeFunctions = new TimeFunctions();

    public adminPane(int loginType, String user) {
        databasedat = Databasedat.getInstance();
        this.loginType = loginType;
        this.user = user;
        loadUI();
    }

    public void loadUI() {
        setLayout(new BorderLayout());
        if (loginType == 1) {
            DefaultTableModel defaultTableModel = databasedat.buildEmployeeSepecificTimeModel(user, serverProp.getStartDate(), serverProp.getStopDate());
            employeeTimeTable = new JTable(defaultTableModel);
            employeeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            //Set time function data
            timeFunctions.setTableModel(employeeTimeTable.getModel());
            timeFunctions.setJTable(employeeTimeTable);
        } else {
            DefaultTableModel defaultTableModel = databasedat.buildEmployeeTimeModel(serverProp.getStartDate(), serverProp.getStopDate());
            employeeTimeTable = new JTable(defaultTableModel);
            employeeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            //Set time function data
            timeFunctions.setTableModel(employeeTimeTable.getModel());
            timeFunctions.setJTable(employeeTimeTable);
        }

        JScrollPane scrollTable = new JScrollPane(employeeTimeTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollTable, BorderLayout.CENTER);

        //Table right click options
        //Check for non manager
        if (loginType != 1) {
            employeeModel.addElement("All Employee's");
            employeeList = new JComboBox<String>(databasedat.getEmployeeList(employeeModel));
            employeeList.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reloadTableModel();
                    timeFunctions.setTableModel(employeeTimeTable.getModel());
                    totalHoursLabel.setText(timeFunctions.calculateTotalHours(false));
                }
            });
            add(employeeList, BorderLayout.NORTH);
            JPopupMenu tableMenu = new JPopupMenu();
            employeeTimeTable.setComponentPopupMenu(tableMenu);
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
            JMenuItem exportTime = new JMenuItem("Export Time");
            tableMenu.add(exportTime);
            exportTime.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    runExport();
                }
            });
            JMenuItem test = new JMenuItem("Test");
            test.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    TableModel testModel = employeeTimeTable.getModel();
                    int numberofColumns = testModel.getColumnCount();
                    int selectedRow = employeeTimeTable.getSelectedRow();
                    for (int i = 0; i < numberofColumns; i++) {

                        if (testModel.getValueAt(selectedRow, i) != null) {
                            System.out.println("Value: " + testModel.getValueAt(selectedRow, i).toString());
                        }
                    }

                }
            });
            tableMenu.add(test);
        }
        //End right click options


        //Panels
        JPanel optionsPanel = new JPanel();
        JPanel southPanel = new JPanel();
        //End Panels

        //SouthPanel stuff
        totalHoursLabel = new JLabel("Total Hours:");
        southPanel.add(totalHoursLabel);
        add(southPanel, BorderLayout.SOUTH);
        //End south panel

        //Options panel
        optionsPanel.setLayout(new GridLayout(0, 1));

        //Add start date
        JLabel dateStartLabel = new JLabel("Start Date");
        dateStart = new JTextField();
        dateStart.setText(serverProp.getStartDate());
        optionsPanel.add(dateStartLabel);
        optionsPanel.add(dateStart);

        //Add end date
        JLabel dateEndLabel = new JLabel("End Date");
        dateStop = new JTextField();
        dateStop.setText(serverProp.getStopDate());
        optionsPanel.add(dateEndLabel);
        optionsPanel.add(dateStop);

        //Check for non manager
        if (loginType != 1) {

            //Update row
            JButton updateRow = new JButton("Update Row");
            optionsPanel.add(updateRow);
            updateRow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSelectedRow();
                }
            });

            //Update All
            JButton updateAll = new JButton("Update All");
            optionsPanel.add(updateAll);
            updateAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateAll();
                }
            });

            //New Record section
            JButton createRecord = new JButton("New Record");
            createRecord.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new NewRecordCreator(adminPane);
                }
            });
            optionsPanel.add(createRecord);

            //Delete record
            JButton deleteRow = new JButton("Delete");
            optionsPanel.add(deleteRow);
            deleteRow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteRow();
                }
            });

            //Reload Time
            JButton updateTimeButton = new JButton("Reload Time");
            optionsPanel.add(updateTimeButton);
            updateTimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    serverProp.saveDateRange(dateStart.getText(), dateStop.getText());
                    reloadTableModel();

                    totalHoursLabel.setText(timeFunctions.calculateTotalHours(false));
                }
            });

            //Export
            JButton exportTime = new JButton("Export");
            optionsPanel.add(exportTime);
            exportTime.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    runExport();
                }
            });

        }

        JButton getTotalHours = new JButton("Total All");
        getTotalHours.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalHoursLabel.setText(timeFunctions.calculateTotalHours(false));
            }
        });

        JButton getSelectedHours = new JButton("Total Selected");
        getSelectedHours.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalHoursLabel.setText(timeFunctions.calculateTotalHours(true));
            }
        });
        optionsPanel.add(getTotalHours);
        optionsPanel.add(getSelectedHours);

        //Create scroll panel
        JScrollPane optionScroll = new JScrollPane();
        optionScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        optionScroll.getViewport().add(optionsPanel);

        //Add scroll panel to main panel
        add(optionScroll, BorderLayout.LINE_START);

        totalHoursLabel.setText(timeFunctions.calculateTotalHours(false));
    }

    public void updateSelectedRow() {
        int row = employeeTimeTable.getSelectedRow();
        String rowId = employeeTimeTable.getValueAt(row, 0).toString();
        String inTime = employeeTimeTable.getValueAt(row, 2).toString();
        Object outTime = employeeTimeTable.getValueAt(row, 3);
        Object note = employeeTimeTable.getValueAt(row, 5);
        if (outTime == null || outTime.toString().isEmpty()) {
            outTime = "NULL";
        } else {
            outTime = outTime.toString();
        }

        if (note == null || note.toString().isEmpty()) {
            note = "";
        } else {
            note = note.toString();
        }

        System.out.println("Value of note = " + note.toString());
        databasedat.updateTime(rowId, inTime, outTime.toString(), note.toString());
        System.out.println("RowID: " + rowId + " inTime: " + inTime + " outTime: " + outTime);
        employeeTimeTable.setValueAt("Data Changed", row, 4);
    }

    public void updateAll() {
        databasedat.massUpdateTime(employeeTimeTable.getModel());
        reloadTableModel();
    }

    public void deleteRow() {
        int response = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected record?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            int row = employeeTimeTable.getSelectedRow();
            System.out.println("Deleting Record: " + employeeTimeTable.getValueAt(row, 0));
            databasedat.deleteRecord(employeeTimeTable.getValueAt(row, 0).toString());
            ((DefaultTableModel) employeeTimeTable.getModel()).removeRow(row);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Delete Canceled");
        }
    }

    public void reloadTableModel() {
        //Check to see what employee model to build
        if (employeeList.getSelectedItem().toString().equals("All Employee's")) {
            //Load all employees
            employeeTimeTable.setModel(databasedat.buildEmployeeTimeModel(dateStart.getText(), dateStop.getText()));
        } else {
            //Load specific employee
            employeeTimeTable.setModel(databasedat.buildEmployeeSepecificTimeModel(employeeList.getSelectedItem().toString(), dateStart.getText(), dateStop.getText()));
        }

        timeFunctions.setTableModel(employeeTimeTable.getModel());
        timeFunctions.setJTable(employeeTimeTable);
    }

    public String[][] buildExport() {

        if (employeeTimeTable.getSelectedRows().length == 0) {
            int rowlist = employeeTimeTable.getRowCount();
            int columnList = employeeTimeTable.getColumnCount();
            String[][] time = new String[rowlist][columnList];
            for (int i = 0; i < rowlist; i++) {
                time[i][0] = employeeTimeTable.getValueAt(i, 1).toString();
                time[i][1] = employeeTimeTable.getValueAt(i, 2).toString();
                //Check incomplete record
                Object h = employeeTimeTable.getValueAt(i, 3);
                if (h == null || h.toString().isEmpty()) {
                    time[i][2] = "Incomplete";
                    time[i][3] = "Incomplete";
                } else {
                    time[i][2] = h.toString();
                    time[i][3] = employeeTimeTable.getValueAt(i, 4).toString();
                }
                return time;
            }

        } else {
            int[] rowlist = employeeTimeTable.getSelectedRows();
            int columnList = employeeTimeTable.getColumnCount();
            String[][] time = new String[rowlist.length][columnList];
            for (int i = 0; i < rowlist.length; i++) {
                time[i][0] = employeeTimeTable.getValueAt(rowlist[i], 1).toString();
                time[i][1] = employeeTimeTable.getValueAt(rowlist[i], 2).toString();
                //Check incomplete record
                Object h = employeeTimeTable.getValueAt(rowlist[i], 3);
                if (h == null || h.toString().isEmpty()) {
                    time[i][2] = "Incomplete";
                    time[i][3] = "Incomplete";
                } else {
                    time[i][2] = h.toString();
                    time[i][3] = employeeTimeTable.getValueAt(rowlist[i], 4).toString();
                }
            }
            return time;
        }
        return null;
    }

    public void runExport() {
        new TimeExport(this);
    }

}


