package com.scholarly.utme.data.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private static final String TAG = "DbConnection: ";
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/assets/databases/jamb_utme.db";
//    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/assets/databases/jamb_utme-en.db";

    private static Connection connection;

    public static Connection getDbConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(DATABASE_URL, "", "KingUrch");

                System.out.println(TAG + "Connection created successfully: " + connection.toString());

            } else {
                System.out.println(TAG + "Retrieved existing Connection : " + connection);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

}
