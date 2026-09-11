package com.scholarly.data.util;

import com.scholarly.MainApplication;
import com.scholarly.util.AppProperties;

import java.sql.Connection;

public class DbConnection {
    private static Connection connection;
    private static SQLiteDatabase db;

    public static Connection getDbConnection() {
        try {
            String dbName = AppProperties.getInstance().getDbName();
            DatabaseBuilder builder = new  DatabaseBuilder(dbName);

            db = builder.getDb();
            connection = builder.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
        }
        return connection;
    }

    public static boolean closeConnection() {
        if (db != null) {
            try {
                connection = null;
                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
