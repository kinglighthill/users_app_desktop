package com.scholarly.utme.data.util;

import com.scholarly.utme.MainApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewDatabase {
    private static final String TAG = "NewDatabase: ";

    private static final String location = MainApplication.class.getResource("/assets/note_syllabus_main.db").toExternalForm();

    private static Connection connection;


    public static boolean isOK() {
        return checkDrivers();
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    /*private static boolean checkConnection() {
        Connection connection = connect();
        System.out.println(TAG + "Connection -> " + connection);
        return connection != null;
    }*/

    public static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";

        try {
            if (connection == null) {
                connection = DriverManager.getConnection(dbPrefix + location);
                System.out.println(TAG + "Connection created successfully: " + connection.toString());
            } else {
                System.out.println(TAG + "Retrieved existing connection -> " + connection);
            }

        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location + " because " + exception.getMessage());
            return null;
        }
        return connection;
    }

}
