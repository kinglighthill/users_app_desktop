package com.scholarly.utme.data.util;

import org.sqlite.SQLiteDataSource;
import org.sqlite.mc.SQLiteMCSqlCipherConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SQLiteDatabase implements AutoCloseable {
    private static final String DB_RESOURCE_NAME = "/assets/databases/waec_ssce.db";

    @FunctionalInterface
    public interface SQLFunction<R> {
        R execute(Connection connection) throws SQLException;
    }

    private final Lock lock = new ReentrantLock();

    private final Path dbFile;
    private final SQLiteDataSource source;

    private Connection connection;
    private boolean open = true;
    private boolean extracted;

    public SQLiteDatabase(Path dbFile) {
        this.dbFile = dbFile.toAbsolutePath().normalize();
        extracted = Files.exists(dbFile);

        source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + this.dbFile);
        source.setEnforceForeignKeys(true);
    }

    public <R> R execute(SQLFunction<R> function) throws SQLException {
        Objects.requireNonNull(function);
        lock.lock();
        try {
            ensureOpen();
            extractDatabase();

            if (connection == null || connection.isClosed()) {
                connection =  SQLiteMCSqlCipherConfig
                        .getV4Defaults()
                        .withKey(Generator.Error())
                        .build()
                        .createConnection(source.getUrl());
            }
            return function.execute(connection);
        } finally {
            lock.unlock();
        }
    }

    private void ensureOpen() {
        if (!open) {
            throw new IllegalStateException("SQLiteDatabase has been closed.");
        }
    }

    private void extractDatabase() {
        if (!extracted) {
            try (var input = SQLiteDatabase.class.getResourceAsStream(DB_RESOURCE_NAME)) {
                Files.copy(input, dbFile);
                extracted = true;
            } catch (FileAlreadyExistsException ex) {
                extracted = true;
            } catch (IOException ex) {
                throw new UncheckedIOException("Unable to extract SQLite database resource", ex);
            }
        }
    }

    @Override
    public void close() throws SQLException {
        lock.lock();
        try {
            if (open) {
                open = false;
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}