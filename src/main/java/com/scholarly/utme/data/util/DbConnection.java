package com.scholarly.utme.data.util;

import com.scholarly.utme.HelloApplication;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private static final String TAG = "DbConnection: ";

    private static final String dbPrefix = "jdbc:sqlite:";
    private static final String location = HelloApplication.class.getResource("/assets/note_syllabus_main.db").toExternalForm();

    private static final String DATABASE_URL = dbPrefix + location;

    private static Connection connection;

    public static Connection getDbConnection(){
        try{
            if (connection == null){
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println(TAG + "Connection created successfully: " + connection.toString());
            } else {
                System.out.println(TAG + "Retrieved existing Connection : " + connection.toString());
            }

        } catch (Exception e){
            System.out.println(e.toString());
        }
        return connection;
    }

}
