package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDao {
    public static final String TAG = "SubjectDao: ";

    private static final String idColumn = "_id";
    private static final String tableNameColumn = "table_name";
    private static final String subjectNameColumn = "subject_name";
    private static final String timeAllottedColumn = "time_alloted";
    private static final String subjectDescriptionColumn = "subject_desc";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String subjectColorColumn = "subject_color";
    private static final String colorNameColumn = "color_name";

    private static final String pqSubjectIdColumn = "subject_id";
    private static final String pqMinutesAllotedColumn = "minutes_alloted";
    private static final String pqOrderColumn = "order";
    private static final String pqTitleColumn = "title";
    private static final String pqShortTitleColumn = "short_title";
    private static final String pqColorCodeColumn = "color_code";


    private static final ObservableList<Subject> subjects;
    private static final ObservableList<PQSubject> pqSubjects;

    static {
        subjects = FXCollections.observableArrayList();
        pqSubjects = FXCollections.observableArrayList();
//        updateSubjectsFromDB();
        updatePQSubjectsFromDB();
    }

    public static String getSubjectName(String subjectShortTitle) {
        String query = "SELECT " + pqTitleColumn + " FROM " + Tables.SUBJECTS + " WHERE " + pqShortTitleColumn + " LIKE '" + subjectShortTitle + "'";

        try {
            Connection connection = DbConnection.getDbConnection();
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            String subjectName = "";
            while (rs.next()) {
                subjectName = rs.getString(pqTitleColumn);
            }
            return subjectName;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subject name from database because " + e.getMessage());
            return null;
        }
    }

    private static void updatePQSubjectsFromDB() {
        String query = "SELECT * FROM " + Tables.PQ_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.PQ_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY 'order'";

        try {
            Connection connection = DbConnection.getDbConnection();
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            pqSubjects.clear();
            while (rs.next()) {
                pqSubjects.add(new PQSubject(
                        rs.getInt(idColumn),
                        rs.getInt(pqSubjectIdColumn),
                        rs.getInt(pqMinutesAllotedColumn),
                        rs.getInt(pqOrderColumn),
                        rs.getString(pqTitleColumn),
                        rs.getString(pqShortTitleColumn),
                        rs.getString(pqColorCodeColumn)));
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subjects from database because " + e.getMessage());
            pqSubjects.clear();
        }
    }

    private static void updateSubjectsFromDB() {
        String query = "SELECT * FROM " + Tables.SUBJECTS;

        try {
            Connection connection = NewDatabase.connect();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            subjects.clear();
            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getInt(idColumn),
                        rs.getString(tableNameColumn),
                        rs.getString(subjectNameColumn),
                        rs.getInt(timeAllottedColumn),
                        rs.getString(subjectDescriptionColumn),
                        rs.getString(shortDescriptionColumn),
                        rs.getString(subjectColorColumn),
                        rs.getString(colorNameColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subjects from database because " + e.getMessage());
            subjects.clear();
        }
    }

    public static ObservableList<Subject> getSubjects() {
        return FXCollections.unmodifiableObservableList(subjects);
    }

    public static ObservableList<PQSubject> getPQSubjects() {
        return FXCollections.unmodifiableObservableList(pqSubjects);
    }

    public static Optional<Subject> getSubject(int id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) return Optional.of(subject);
        }
        return Optional.empty();
    }
}
