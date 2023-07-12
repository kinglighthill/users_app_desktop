package com.scholarly.utme.data;

import com.scholarly.utme.data.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {
    private static final String TAG = "DatabaseService: ";

    private static final Connection connection = DbConnection.getDbConnection();

    public ResultSet executeQuery(String query) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public long executeUpdate(String query) throws Exception {
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
            return -1;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
