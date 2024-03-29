package com.scholarly.data.util;

import com.scholarly.util.AppProperties;
import com.scholarly.util.Helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

public class DatabaseBuilder {
    private Connection connection;
    private SQLiteDatabase db;

    private Path dbFile;

    public DatabaseBuilder(String dbName) {
        try {
            dbFile = getDbPath(dbName);
            Files.createDirectories(dbFile.getParent());
            db = new SQLiteDatabase(dbFile);
            connection = db.execute(conn -> conn);
        } catch (Exception e) {
            System.out.println("Hmm: " + e.getMessage());
        }
    }

    private Path getDbPath(String dbName) {
        String appName = AppProperties.getInstance().getAppName();

        if (Helper.isOsType(Helper.OS_TYPE.WIN)) {
            return Path.of(System.getenv("LOCALAPPDATA"), appName, dbName);
        } else if (Helper.isOsType(Helper.OS_TYPE.MAC)) {
            String pathUrl = System.getProperty("user.home") + "/Library/Application Support/";
            return Path.of(pathUrl, appName, dbName);
        } else {
            return Path.of(System.getProperty("user.home"), appName, dbName);
        }
    }

    private boolean closeConnection() {
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

    public Connection getConnection() {
        return connection;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public String getPath() {
        return dbFile.toString();
    }

    public boolean renameDb(File dest) {
        if (closeConnection()) {
            return dbFile.toFile().renameTo(dest);
        } else {
            return false;
        }
    }

    public boolean deleteDb() {
        if (closeConnection()) {
            return dbFile.toFile().delete();
        } else {
            return false;
        }
    }
}
