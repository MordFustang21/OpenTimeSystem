package com.timesystem;

import com.timesystem.gui.PinScreen;
import com.timesystem.mod.DatabaseCreater;

import javax.swing.*;

public class TimeMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        if (args.length == 0) {
            new PinScreen();
        } else if (args[0].equals("-new")) {
            new DatabaseCreater().create(args[1], args[2]);
        }
    }
}
