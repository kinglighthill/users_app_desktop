package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.ObjectiveSubject;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.newDb.TheorySubject;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDao {
    public static final String TAG = "SubjectDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String tableNameColumn = "table_name";
    private static final String subjectNameColumn = "subject_name";
    private static final String timeAllottedColumn = "time_alloted";
    private static final String subjectDescriptionColumn = "subject_desc";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String subjectColorColumn = "subject_color";
    private static final String colorNameColumn = "color_name";

    private static final String subjectIdColumn = "subject_id";
    private static final String minutesAllotedColumn = "minutes_alloted";
    private static final String orderColumn = "order";
    private static final String titleColumn = "title";
    private static final String shortTitleColumn = "short_title";
    private static final String colorCodeColumn = "color_code";
    public static final String descriptionColumn = "description";

    private static final ObservableList<Subject> subjects;
    private static final ObservableList<PQSubject> pqSubjects;
    private static final ObservableList<ObjectiveSubject> objectiveSubjects;
    private static final ObservableList<TheorySubject> theorySubjects;

    static {
        subjects = FXCollections.observableArrayList();
        pqSubjects = FXCollections.observableArrayList();
        objectiveSubjects = FXCollections.observableArrayList();
        theorySubjects = FXCollections.observableArrayList();
//        updateSubjectsFromDB();
//        updatePQSubjectsFromDB();
        updateObjectiveSubjectsFromDb();
        updateTheorySubjectsFromDb();
    }

    public static String getSubjectName(String subjectShortTitle) {
        String query = "SELECT " + titleColumn + " FROM " + Tables.SUBJECTS + " WHERE " + shortTitleColumn + " LIKE '" + subjectShortTitle + "'";

        try {
            Connection connection = DbConnection.getDbConnection();
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            String subjectName = "";
            while (rs.next()) {
                subjectName = rs.getString(titleColumn);
            }
            return subjectName;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subject name from database because " + e.getMessage());
            return null;
        }
    }

    private static void updateObjectiveSubjectsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.PQ_OBJECTIVE_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY 'order'";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            objectiveSubjects.clear();

            while (rs.next()) {
                objectiveSubjects.add(new ObjectiveSubject(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(minutesAllotedColumn),
                        rs.getInt(orderColumn),
                        rs.getString(titleColumn),
                        rs.getString(shortTitleColumn),
                        rs.getString(descriptionColumn),
                        rs.getString(colorCodeColumn)));
            }

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Objective Subjects from database because " + e.getMessage());
            objectiveSubjects.clear();
        }
    }

    private static void updateTheorySubjectsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_THEORY_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.PQ_THEORY_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY 'order'";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            theorySubjects.clear();

            while (rs.next()) {
                theorySubjects.add(new TheorySubject(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(minutesAllotedColumn),
                        rs.getInt(orderColumn),
                        rs.getString(titleColumn),
                        rs.getString(shortTitleColumn),
                        rs.getString(descriptionColumn),
                        rs.getString(colorCodeColumn)));
            }

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Theory Subjects from database because " + e.getMessage());
            theorySubjects.clear();
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
                        rs.getInt(subjectIdColumn),
                        rs.getInt(minutesAllotedColumn),
                        rs.getInt(orderColumn),
                        rs.getString(titleColumn),
                        rs.getString(shortTitleColumn),
                        rs.getString(colorCodeColumn)));
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

    public static ObservableList<ObjectiveSubject> getObjectiveSubjects() {
        return FXCollections.unmodifiableObservableList(objectiveSubjects);
    }

    public static ObservableList<TheorySubject> getTheorySubjects() {
        return FXCollections.unmodifiableObservableList(theorySubjects);
    }

    public static Optional<Subject> getSubject(int id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) return Optional.of(subject);
        }
        return Optional.empty();
    }
}
