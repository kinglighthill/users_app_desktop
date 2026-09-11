package com.scholarly.utme.data.util;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.util.Helper;
import org.sqlite.mc.HmacAlgorithm;
import org.sqlite.mc.KdfAlgorithm;
import org.sqlite.mc.SQLiteMCConfig;
import org.sqlite.mc.SQLiteMCSqlCipherConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class DbConnection {
    private static final String TAG = "DbConnection: ";

    private static Connection connection;

    public static Connection getDbConnection() {
        try {
            MainApplication.logInfo("Database Connecting..");

            Path dbFile;
            if (Helper.isOsType(Helper.OS_TYPE.WIN)) {
                dbFile = Path.of(System.getenv("LOCALAPPDATA"), "Scholarly JAMB CBT", "jamb_utme.db");
            } else if (Helper.isOsType(Helper.OS_TYPE.MAC)) {
                String pathUrl = System.getProperty("user.home") + "/Library/Application Support/";
                dbFile = Path.of(pathUrl, "Scholarly JAMB CBT", "jamb_utme.db");
            } else {
                dbFile = Path.of(System.getProperty("user.home"), "Scholarly JAMB CBT", "jamb_utme.db");
            }

            Files.createDirectories(dbFile.getParent());
            var db = new SQLiteDatabase(dbFile);

            connection = db.execute(conn -> conn);

            System.out.println(TAG + "Connection created successfully: " + connection.toString());
            MainApplication.logInfo(TAG + "Connection created successfully: " + connection.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
        }
        return connection;
    }
}
