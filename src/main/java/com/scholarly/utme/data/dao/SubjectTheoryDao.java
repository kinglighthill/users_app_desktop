package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.SubjectTheory;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.Table;
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

public class SubjectTheoryDao {

    private static final String tableName = Table.SubjectsTheory.getTableName();

    private static final String idColumn = "_id";
    private static final String tableNameColumn = "table_name";
    private static final String subjectNameColumn = "subject_name";
    private static final String timeAllottedColumn = "time_alloted";
    private static final String subjectDescriptionColumn = "subject_desc";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String subjectColorColumn = "subject_color";
    private static final String colorNameColumn = "color_name";


    private static final ObservableList<Subject> subjectsTheory;

    static {
        subjectsTheory = FXCollections.observableArrayList();
        updateSubjectsTheoryFromDB();
    }

    private static void updateSubjectsTheoryFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            subjectsTheory.clear();
            while (rs.next()) {
                subjectsTheory.add(new Subject(
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
            subjectsTheory.clear();
        }
    }

    public static ObservableList<Subject> getSubjectsTheory() {
        return FXCollections.unmodifiableObservableList(subjectsTheory);
    }

    public static Optional<Subject> getSubjectTheory(int id) {
        for (Subject subject : subjectsTheory) {
            if (subject.getId() == id) return Optional.of(subject);
        }
        return Optional.empty();
    }
}
