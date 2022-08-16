package com.scholarly.utme.data.util;

import com.scholarly.utme.HelloApplication;
import com.scholarly.utme.data.dao.BookmarkDao;
import com.scholarly.utme.data.dao.ObjectiveBookmarkDao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {

    public static final String TAG = "Database: ";

    /**
     * Location of database
     */
    private static final String location = HelloApplication.class.getResource("/assets/databaseFile.db").toExternalForm();


    /**
     * Table Constants
     */
    private static final String requiredTable = "Persons";
    private static final String BOOKMARKS_OBJECTIVE_QUESTIONS = "bookmarks_objective_questions";
    private static final String ID_COLUMN = "_id";


    public static boolean isOK() {

        /*if (!ObjectiveBookmarkDao.checkTable()) {
            ObjectiveBookmarkDao.createTable();
        }*/

        return checkDrivers() && checkConnection();

        /*if (!checkDrivers()) return false; //driver errors

        if (!checkConnection()) return false; //can't connect to db

//        if (!initialize()) return false;

        return true;*/

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
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database because " + e.getMessage());
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


    /*private static boolean createTables() {

        String sql = "CREATE TABLE IF NOT EXISTS " + BOOKMARKS_OBJECTIVE_QUESTIONS +
                " ( _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year_id INTEGER NOT NULL, " +
                "question_id INTEGER NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(_id), " +
                "FOREIGN KEY(year_id) REFERENCES years(_id), " +
                "UNIQUE(subject_id, year_id, question_id) " +
                ");";

        try (Connection conn = DriverManager.getConnection(location);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }*/

    public static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
//            System.out.println(TAG + "Connection object -> " + connection);
//            DatabaseMetaData metaData = connection.getMetaData();
//            System.out.println(TAG + "Metadata username -> " + metaData.getUserName() + " and connection -> " + metaData.getConnection());
//            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location + " because " + exception.getMessage());
            return null;
        }
        return connection;
    }


}
