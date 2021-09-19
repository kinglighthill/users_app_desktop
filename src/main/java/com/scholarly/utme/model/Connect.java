package com.scholarly.utme.model;

import java.sql.*;

public class Connect {
    private static Connection connection;

    public static boolean connect(String dbPath) {
        try {
//            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return true;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void populateDb() {
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("drop table if exists subjects;");
            stat.executeUpdate("CREATE TABLE \"subjects\" (\n" +
                    "\t\"_id\"\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t\"table_name\"\tTEXT UNIQUE,\n" +
                    "\t\"subject_name\"\tTEXT,\n" +
                    "\t\"subject_desc\"\tTEXT,\n" +
                    "\t\"short_desc\"\tTEXT,\n" +
                    "\t\"subject_color\"\tTEXT,\n" +
                    "\t\"color_name\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
                    ")");

            PreparedStatement prep = connection.prepareStatement(
                    "insert into subjects values (?, ?, ?, ?, ?, ?, ?);");

            prep.setString(2, "english");
            prep.setString(3, "English Language");
            prep.setString(4, null);
            prep.setString(5, "Eng");
            prep.setString(6, "#FF5733");
            prep.setString(7, null);
            prep.addBatch();

            prep.setString(2, "mathematics");
            prep.setString(3, "Mathematics");
            prep.setString(4, null);
            prep.setString(5, "Maths");
            prep.setString(6, "#2D7CD5");
            prep.setString(7, null);
            prep.addBatch();

            connection.setAutoCommit(false);
            prep.executeBatch();
            connection.setAutoCommit(true);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static ResultSet getSubjectNames() {
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM subjects");
            System.out.println("hey");
            return set;

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return null;
        }
    }
}