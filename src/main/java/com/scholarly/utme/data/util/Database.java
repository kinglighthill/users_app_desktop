package com.scholarly.utme.data.util;

import com.scholarly.utme.HelloApplication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {

    /**
     * Location of database
     */
    private static final String location = HelloApplication.class.getResource("/assets/databaseFile.db").toExternalForm();


    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Persons";

    public static boolean isOK() {
        if (!checkDrivers()) return false; //driver errors

        if (!checkConnection()) return false; //can't connect to db

//        if (!initialize()) return false;

        return true;

//        return checkTables(); //tables didn't exist
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

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database");
            return false;
        }
    }

//    private static boolean checkTables() {
//        String checkTables =
//                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + requiredTable + "'";
//
//        try (Connection connection = Database.connect()) {
//            PreparedStatement statement = connection.prepareStatement(checkTables);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                if (rs.getString("tbl_name").equals(requiredTable)) return true;
//            }
//        } catch (SQLException exception) {
//            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not find tables in database");
//            return false;
//        }
//        return false;
//    }


    private static boolean initialize() {
        // SQLite connection string

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS bookmarks (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	questionId integer,\n"
                + "	subjectId integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(location);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location);
            return null;
        }
        return connection;
    }

}
