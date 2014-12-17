package com.timesystem.mod;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ServerProp {
    Properties prop = new Properties();

    OutputStream outputStream = null;
    InputStream inputStream = null;

    InputStream timeStream = null;

    String propFile = "server.properties";
    String compProp = "company.properties";
    String timeManager = "timeManager.properties";

    public ServerProp() {
        if (new File(propFile).exists()) {
            try {
                inputStream = new FileInputStream(propFile);
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("server.properties does not exist");
        }

        if (new File(timeManager).exists()) {
            try {
                timeStream = new FileInputStream(timeManager);

            } catch (IOException i) {
                i.printStackTrace();
            }
        } else {
            System.err.println("timeManager.properties doesn't exist");
        }
    }

    public void saveProperties(String server, String port, String username, String password, String database) {

        try {
            System.out.println("Trying save");
            outputStream = new FileOutputStream(propFile);

            prop.setProperty("server", server);
            prop.setProperty("port", port);
            prop.setProperty("username", username);
            prop.setProperty("password", password);
            prop.setProperty("database", database);

            prop.store(outputStream, null);
            System.out.println("Save Successful");
        } catch (IOException f) {
            f.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        }

    }


    public void saveCompanyName(String compName) {
        try {
            System.out.println("Trying to save Company Name");
            outputStream = new FileOutputStream(compProp);
            prop.setProperty("compName", compName);

            prop.store(outputStream, null);
            System.out.println("Save Successful");
        } catch (IOException i) {
            i.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        }

    }

    public String getCompName() {
        if (new File(compProp).exists()) {
            try {
                inputStream = new FileInputStream(compProp);

                prop.load(inputStream);

                return prop.getProperty("compName");
            } catch (IOException i) {
                i.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                }
            }
        }
        return "not set";
    }

    public String getServer() {
        return prop.getProperty("server");
    }

    public String getPort() {
        return prop.getProperty("port");
    }

    public String getUsername() {
        return prop.getProperty("username");
    }

    public String getPassword() {
        return prop.getProperty("password");
    }

    public String getDatabase() {
        return prop.getProperty("database");
    }


    public void saveDateRange(String startDate, String stopDate) {
        System.out.println("Saving Time Manager Settings");
        try {
            outputStream = new FileOutputStream(timeManager);
            prop.setProperty("startDate", startDate);
            prop.setProperty("stopDate", stopDate);
            prop.store(outputStream, null);

            System.out.println("Save Successful");
        } catch (IOException i) {
            i.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        }

    }

    public String getStopDate() {
        try {
            prop.load(timeStream);

            String stopDate = prop.getProperty("stopDate");
            System.err.println(stopDate);
            if(stopDate == null || stopDate.equals("")){
                return getCurrentDate();
            } else {
                return stopDate;
            }
        } catch (IOException i) {
            i.printStackTrace();
        }

        return getCurrentDate();
    }

    public String getStartDate() {
        try {
            prop.load(timeStream);

            String startDate = prop.getProperty("startDate");
            System.err.println(startDate);
            if(startDate == null || startDate.equals("")){
                return getCurrentDate();
            } else {
                return startDate;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getCurrentDate();
    }

    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
