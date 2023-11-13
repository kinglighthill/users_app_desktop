package com.scholarly.utme.data.util;

import com.scholarly.utme.MainApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class DbConnection {
    private static final String TAG = "DbConnection: ";
    private static final String location = MainApplication.class.getResource("/assets/databases/jamb_utme.db").getPath();
    private static final String DATABASE_URL = "jdbc:sqlite:"+location;

    private static Connection connection;

    /*public static Connection getDbConnection() {
        try {
            Path mydb = Path.of(System.getenv("LOCALAPPDATA"), "Scholarly JAMB CBT", "jamb_utme.db");
            final String resName= "/assets/databases/jamb_utme.db";
            if (!Files.isRegularFile(mydb)) {
                Files.createDirectories(mydb.getParent());
                try(var in = DbConnection.class.getResourceAsStream(resName)) {
                    Objects.requireNonNull(in, () -> "Not found resource: "+resName);
                    Files.copy(in, mydb);
                }
            }

            if (connection == null) {
                MainApplication.logInfo("Database Connecting..");
//                connection = DriverManager.getConnection(DATABASE_URL, "", "");
                connection = DriverManager.getConnection("jdbc:sqlite:"+mydb.toAbsolutePath());

                System.out.println(TAG + "Connection created successfully: " + connection.toString());
                MainApplication.logInfo(TAG + "Connection created successfully: " + connection.toString());
            } else {
                System.out.println(TAG + "Retrieved existing Connection : " + connection);
                MainApplication.logInfo(TAG + "Retrieved existing Connection : " + connection);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
        }
        return connection;
    }*/

    public static Connection getDbConnection() {
        try {
//            var dbFile = Path.of(System.getProperty("user.home"), "Scholarly JAMB CBT", "jamb_utme.db");
            var dbFile = Path.of(System.getenv("LOCALAPPDATA"), "Scholarly JAMB CBT", "jamb_utme.db");
            Files.createDirectories(dbFile.getParent());
            var db = new SQLiteDatabase(dbFile);

            connection = db.execute(conn -> conn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
        }
        return connection;
    }
}
