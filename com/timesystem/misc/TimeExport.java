package com.timesystem.misc;

import com.timesystem.gui.Panels.*;

import javax.swing.*;


public class TimeExport extends JFrame {

    public TimeExport(adminPane adminPane) {
        setTitle("Export Preview");
        setContentPane(new TimeExportPane(adminPane, this));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(960, 540);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}


