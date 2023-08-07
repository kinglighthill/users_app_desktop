package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.FreeContent;
import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class YearsDao {
    private static final String TAG = "YearsDao: ";

    private static final DatabaseService databaseService = new DatabaseService();
    private static final Preferences preferences = AppPreferences.getPreferences();

    private static final String idColumn = "_id";
    private static final String yearColumn = "year";
    private static final String yearIdColumn = "year_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";

    private static final ObservableList<Year> allYears;
    private static final ObservableList<Year> subjectAvailableYears;
    private static final ObservableList<FreeContent> freeContents;

    static {
        allYears = FXCollections.observableArrayList();
        subjectAvailableYears = FXCollections.observableArrayList();
        freeContents = FXCollections.observableArrayList();
        updateYearsFromDB();
        updateFreeYearsColumn();
    }

    public static ObservableList<Year> getAvailableYearsForSubject(SubjectListItemVM.Type type, int subjectId) {
        String query = "";

        if (type == SubjectListItemVM.Type.OBJECTIVE) {
            query = "SELECT DISTINCT " + Tables.YEARS + "." + idColumn + ", " + yearColumn + ", " + shortDescriptionColumn + ", " + isNewColumn + ", " + availableColumn + " FROM " + Tables.YEARS + " JOIN " + Tables.PQ_OBJECTIVE_QUESTIONS + " ON " + Tables.PQ_OBJECTIVE_QUESTIONS + "." + yearIdColumn + " = " + Tables.YEARS + "." + idColumn + " WHERE " + subjectIdColumn + " = " + subjectId + " ORDER BY " + yearIdColumn + " DESC";

        } else if (type == SubjectListItemVM.Type.THEORY){
            query = "SELECT DISTINCT " + Tables.YEARS + "." + idColumn + ", " + yearColumn + ", " + shortDescriptionColumn + ", " + isNewColumn + ", " + availableColumn + " FROM " + Tables.YEARS + " JOIN " + Tables.PQ_THEORY_QUESTIONS + " ON " + Tables.PQ_THEORY_QUESTIONS + "." + yearIdColumn + " = " + Tables.YEARS + "." + idColumn + " WHERE " + subjectIdColumn + " = " + subjectId + " ORDER BY " + yearIdColumn + " DESC";

        }

//        System.out.println(TAG + "Query -> " + query);

//        System.out.println(TAG + "Available Years For Subject with id -> " + subjectId + " Query -> " + query + " AND Type -> " + type);

        try (ResultSet rs = databaseService.executeQuery(query)) {
            subjectAvailableYears.clear();
            while (rs.next()) {
                if (preferences.getBoolean(Constants.PREF_KEY_ACTIVATION_STATE, false)) {
                    subjectAvailableYears.add(
                            new Year(
                                    rs.getInt(idColumn),
                                    rs.getString(yearColumn),
                                    rs.getString(shortDescriptionColumn),
                                    rs.getInt(isNewColumn),
                                    rs.getInt(availableColumn),
                                    true)
                    );
                } else {
                    subjectAvailableYears.add(
                            new Year(
                                    rs.getInt(idColumn),
                                    rs.getString(yearColumn),
                                    rs.getString(shortDescriptionColumn),
                                    rs.getInt(isNewColumn),
                                    rs.getInt(availableColumn),
                                    false)
                    );
                    for (Year year : subjectAvailableYears) {
                        for (FreeContent content : freeContents) {
                            if (content.getYearId() == year.getId()) {
                                year.setFree(true);
                            }
                        }
                    }
                }
            }

            return subjectAvailableYears;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load availableYears from database because " + e.getMessage());
            subjectAvailableYears.clear();
            return null;
        }

    }

    private static void updateYearsFromDB() {
        String query = "SELECT * FROM " + Tables.YEARS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            allYears.clear();
            while (rs.next()) {
                allYears.add(new Year(
                        rs.getInt(idColumn),
                        rs.getString(yearColumn),
                        rs.getString(shortDescriptionColumn),
                        rs.getInt(isNewColumn),
                        rs.getInt(availableColumn),
                        false));
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Years from database because " + e.getMessage());
            allYears.clear();
        }
    }

    private static void updateFreeYearsColumn() {
        String query = "SELECT * FROM " + Tables.FREE_CONTENTS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            freeContents.clear();
            while (rs.next()) {
                freeContents.add(new FreeContent(
                        rs.getInt(idColumn),
                        rs.getInt("subject_id"),
                        rs.getInt("year_id"),
                        rs.getInt("topic_id"),
                        rs.getInt("chapter_id")));

            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Free Contents from database because " + e.getMessage());
            freeContents.clear();
        }
    }

    public static ObservableList<Year> getYears() {
        return FXCollections.unmodifiableObservableList(subjectAvailableYears);
    }

    public static Optional<Year> getYear(int id) {
        for (Year year : allYears) {
            if (year.getId() == id) return Optional.of(year);
        }
        return Optional.empty();
    }
}
