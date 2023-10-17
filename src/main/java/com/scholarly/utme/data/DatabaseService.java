package com.scholarly.utme.data;

import com.scholarly.utme.data.util.DbConnection;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {
    private static final String TAG = "DatabaseService: ";

    private static final Connection connection = DbConnection.getDbConnection();

    public ResultSet executeQuery(String query) throws Exception {
        Task<ResultSet> task = new Task<>() {
            @Override
            protected ResultSet call() throws Exception {
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    return statement.executeQuery();
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        return task.get();
    }

    public long executeUpdate(String query) throws Exception {
        Task<Long> task = new Task<>() {
            @Override
            protected Long call() throws Exception {
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    int affectedRows = statement.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet rs = statement.getGeneratedKeys()) {
                            if (rs.next()) {
                                return rs.getLong(1);
                            }
                        }
                    }
                    return Integer.toUnsignedLong(-1);
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        return task.get();
    }

    public int delete(String query) throws Exception {
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                try {
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    return pstmt.executeUpdate();

                } catch (SQLException e) {
                    Logger.getAnonymousLogger().log(
                            Level.SEVERE,
                            LocalDateTime.now() + ": Could not delete from database because " + e.getMessage());
                    return -1;
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        return task.get();
    }
}
