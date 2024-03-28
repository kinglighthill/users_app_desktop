package com.scholarly.data.util;

import com.scholarly.MainApplication;
import com.scholarly.util.AppProperties;
import com.scholarly.util.Helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

public class DbConnection {
    private static final String TAG = "DbConnection: ";

    private static Connection connection;
    private static SQLiteDatabase db;

    public static Connection getDbConnection() {
        try {
            MainApplication.logInfo("Database Connecting..");

            String appName = AppProperties.getInstance().getAppName();
            String dbName = AppProperties.getInstance().getDbName();

            Path dbFile;
            if (Helper.isOsType(Helper.OS_TYPE.WIN)) {
                dbFile = Path.of(System.getenv("LOCALAPPDATA"), appName, dbName);
            } else if (Helper.isOsType(Helper.OS_TYPE.MAC)) {
                String pathUrl = System.getProperty("user.home") + "/Library/Application Support/";
                dbFile = Path.of(pathUrl, appName, dbName);
            } else {
                dbFile = Path.of(System.getProperty("user.home"), appName, dbName);
            }

            Files.createDirectories(dbFile.getParent());
            db = new SQLiteDatabase(dbFile);

            connection = db.execute(conn -> conn);

            System.out.println(TAG + "Connection created successfully: " + connection.toString());
            MainApplication.logInfo(TAG + "Connection created successfully: " + connection.toString());
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
