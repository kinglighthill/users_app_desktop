package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YearsDao {
    private static final String TAG = "YearsDao: ";

    private static final String tableName = "years";

    private static final String idColumn = "_id";
    private static final String yearColumn = "year";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";

    private static final String tableNamePlusIdColumn = tableName + "." + idColumn;

    private static final ObservableList<Year> years;

    private static final ObservableList<Year> availableYears;

    static {
        years = FXCollections.observableArrayList();
        availableYears = FXCollections.observableArrayList();
        updateYearsFromDB();

    }

    public static ObservableList<Year> getAvailableYearsForSubject(SubjectListItemVM.Type type, int subjectId) {
        ObservableList<Year> subjectAvailableYears = FXCollections.observableArrayList();

        String query = "";

        if (type == SubjectListItemVM.Type.OBJECTIVE) {
            query = "SELECT DISTINCT " + Tables.YEARS + "." + idColumn + ", " + yearColumn + ", " + shortDescriptionColumn + ", " + isNewColumn + ", " + availableColumn + " FROM " + Tables.YEARS + " JOIN " + Tables.PQ_OBJECTIVE_QUESTIONS + " ON " + Tables.PQ_OBJECTIVE_QUESTIONS + ".year_id = " + Tables.YEARS + "." + idColumn + " WHERE subject_id = " + subjectId;

        } else if (type == SubjectListItemVM.Type.THEORY){
            query = "SELECT DISTINCT " + Tables.YEARS + "." + idColumn + ", " + yearColumn + ", " + shortDescriptionColumn + ", " + isNewColumn + ", " + availableColumn + " FROM " + Tables.YEARS + " JOIN " + Tables.PQ_THEORY_QUESTIONS + " ON " + Tables.PQ_THEORY_QUESTIONS + ".year_id = " + Tables.YEARS + "." + idColumn + " WHERE subject_id = " + subjectId;

        }

//        System.out.println(TAG + "Available Years For Subject with id -> " + subjectId + " Query -> " + query + " AND Type -> " + type);

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                subjectAvailableYears.add(
                        new Year(
                                rs.getInt(idColumn),
                                rs.getString(yearColumn),
                                rs.getString(shortDescriptionColumn),
                                rs.getInt(isNewColumn),
                                rs.getInt(availableColumn))
                );

            }
           // System.out.println(subjectId + " available years -> " + subjectAvailableYears);
            return subjectAvailableYears;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load availableYears from database because " + e.getMessage());
            subjectAvailableYears.clear();
            return null;
        }

    }

    private static void updateYearsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = NewDatabase.connect()) {
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

    public static ObservableList<Year> getAvailableYears() {
        return FXCollections.unmodifiableObservableList(availableYears);
    }

    public static Optional<Boolean> isYearAvailable(int id) {
        for (Year year : availableYears) {
            if (year.getId() == id) return Optional.of(true);
        }
        return Optional.of(false);
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
