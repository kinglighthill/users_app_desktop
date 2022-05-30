package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDao {

    private static final String tableName = "subjects";

    private static final String idColumn = "_id";
    private static final String tableNameColumn = "table_name";
    private static final String subjectNameColumn = "subject_name";
    private static final String timeAllottedColumn = "time_alloted";
    private static final String subjectDescriptionColumn = "subject_desc";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String subjectColorColumn = "subject_color";
    private static final String colorNameColumn = "color_name";


    private static final ObservableList<Subject> subjects;

    static {
        subjects = FXCollections.observableArrayList();
        updateSubjectsFromDB();
    }

    public static String getSubjectName(String subjectTableName) {
        String query = "SELECT " + subjectNameColumn + " FROM " + tableName + " WHERE " + tableNameColumn + " = '" + subjectTableName + "'";
//        System.out.println(query);

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            String subjectName = "";
            while (rs.next()) {
                subjectName = rs.getString(subjectNameColumn);
            }
            return subjectName;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subjects from database ");
            return null;
        }
    }

    private static void updateSubjectsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
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
                    LocalDateTime.now() + ": Could not load Subjects from database ");
            subjects.clear();
        }
    }

    public static ObservableList<Subject> getSubjects() {
        return FXCollections.unmodifiableObservableList(subjects);
    }

    public static Optional<Subject> getSubject(int id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) return Optional.of(subject);
        }
        return Optional.empty();
    }
}
