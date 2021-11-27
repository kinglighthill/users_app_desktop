package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.data.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YearsDao {

    private static final String tableName = "years";

    private static final String idColumn = "_id";
    private static final String yearColumn = "year";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";

    private static final ObservableList<Year> years;

    static {
        years = FXCollections.observableArrayList();
        updateYearsFromDB();
    }

    private static void updateYearsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            years.clear();
            while (rs.next()) {
                years.add(new Year(
                        rs.getInt(idColumn),
                        rs.getString(yearColumn),
                        rs.getString(shortDescriptionColumn),
                        rs.getInt(isNewColumn),
                        rs.getInt(availableColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Years from database ");
            years.clear();
        }
    }

    public static ObservableList<Year> getYears() {
        return FXCollections.unmodifiableObservableList(years);
    }

    public static Optional<Year> getYear(int id) {
        for (Year year : years) {
            if (year.getId() == id) return Optional.of(year);
        }
        return Optional.empty();
    }
}
