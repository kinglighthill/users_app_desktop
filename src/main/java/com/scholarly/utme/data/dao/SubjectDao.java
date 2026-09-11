package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ListIterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDao {
    private static final String TAG = "SubjectDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String userIdColumn = "uid";
    private static final String tableNameColumn = "table_name";
    private static final String subjectNameColumn = "subject_name";
    private static final String timeAllottedColumn = "time_alloted";
    private static final String subjectDescriptionColumn = "subject_desc";
    private static final String shortDescriptionColumn = "short_desc";
    private static final String subjectColorColumn = "subject_color";
    private static final String colorNameColumn = "color_name";

    private static final String subjectIdColumn = "subject_id";
    private static final String minutesAllottedColumn = "minutes_allotted";
    private static final String orderColumn = "order";
    private static final String titleColumn = "title";
    private static final String shortTitleColumn = "short_title";
    private static final String colorCodeColumn = "color_code";
    public static final String descriptionColumn = "description";

    private static final ObservableList<Subject> subjects;
    private static final ObservableList<ObjectiveSubject> objectiveSubjects;
    private static final ObservableList<TheorySubject> theorySubjects;
    private static final ObservableList<NoteSubject> noteSubjects;

    private static final ObservableList<FavoriteSubject> favoriteSubjects;

    static {
        subjects = FXCollections.observableArrayList();
        objectiveSubjects = FXCollections.observableArrayList();
        theorySubjects = FXCollections.observableArrayList();
        noteSubjects = FXCollections.observableArrayList();
        favoriteSubjects = FXCollections.observableArrayList();
//        updateSubjectsFromDB();
        updateObjectiveSubjectsFromDb();
        updateTheorySubjectsFromDb();
        updateNoteSubjectsFromDb();
        updateFavoriteSubjectsFromDb();
    }

    public static String getSubjectName(String subjectShortTitle) {
        String query = "SELECT " + titleColumn + " FROM " + Tables.SUBJECTS + " WHERE " + shortTitleColumn + " LIKE '" + subjectShortTitle + "'";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            String subjectName = "";
            while (rs.next()) {
                subjectName = rs.getString(titleColumn);
            }
            return subjectName;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subject name from database because " + e.getMessage());
            return null;
        }
    }

    private static void updateObjectiveSubjectsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.PQ_OBJECTIVE_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY 'order'";
        System.out.println(TAG + "Objective Subjects Query -> " + query);

        try (ResultSet rs = databaseService.executeQuery(query)) {
            objectiveSubjects.clear();

            while (rs.next()) {
                objectiveSubjects.add(new ObjectiveSubject(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(minutesAllottedColumn),
                        rs.getInt(orderColumn),
                        rs.getString(titleColumn),
                        rs.getString(shortTitleColumn),
                        rs.getString(colorCodeColumn),
                        rs.getString(descriptionColumn)));
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
                        rs.getInt(minutesAllottedColumn),
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

    private static void updateNoteSubjectsFromDb() {
        String query = "SELECT " + Tables.NOTE_SUBJECTS + "." + idColumn + ", " + Tables.NOTE_SUBJECTS + "." + subjectIdColumn + ", " + Tables.NOTE_SUBJECTS + ".'" + orderColumn + "', " + Tables.SUBJECTS + "." + titleColumn + ", " + Tables.SUBJECTS + "." + shortTitleColumn + ", " + Tables.SUBJECTS + "." + colorCodeColumn + " FROM " + Tables.NOTE_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.NOTE_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY " + "\"order\"";
//        System.out.println(TAG + "Note Subjects Query -> " + query);

        try (ResultSet rs = databaseService.executeQuery(query)) {
            noteSubjects.clear();
            while (rs.next()) {
                noteSubjects.add(new NoteSubject(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(orderColumn),
                        rs.getString(shortTitleColumn),
                        rs.getString(colorCodeColumn)));
            }

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subjects from database because " + e.getMessage());
            noteSubjects.clear();
        }
    }

    private static void updateFavoriteSubjectsFromDb() {
        for (ObjectiveSubject objectiveSubject : objectiveSubjects) {
            favoriteSubjects.add(new FavoriteSubject(
                    objectiveSubject.getId(),
                    objectiveSubject.getSubjectId(),
                    objectiveSubject.getMinutesAllotted(),
                    objectiveSubject.getOrder(),
                    objectiveSubject.getTitle(),
                    objectiveSubject.getShortTitle(),
                    objectiveSubject.getColorCode(),
                    objectiveSubject.getDescription(),
                    false
            ));
        }
    }

    public static void insertSubjectCombination(SubjectCombination subjectCombination) {
        String query = CRUDHelper.insertQuery(
                Tables.SUBJECTS_COMBINATION,
                new String[]{"subject_id", "uid"},
                new Object[]{subjectCombination.getSubjectId(), subjectCombination.getUserId()},
                new int[]{Types.INTEGER, Types.VARCHAR});

        try {
            databaseService.executeUpdate(query);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not insert item to database because " + ex.getMessage());
        }

    }

    public static ObservableList<SubjectCombination> retrieveSubjectCombination(String userId) {
        String query = "SELECT * FROM " + Tables.SUBJECTS_COMBINATION + " WHERE " + userIdColumn + " = '" + userId + "'";

        ObservableList<SubjectCombination> subjectCombinations = FXCollections.observableArrayList();

        try (ResultSet rs = databaseService.executeQuery(query)) {
            while (rs.next()) {
                subjectCombinations.add(new SubjectCombination(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getString(userIdColumn)));
            }

            return subjectCombinations;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Favorite Subjects from database because " + e.getMessage());
            return subjectCombinations;
        }

    }

    public static void deletePreviousSubjectCombination(String userId) {
        String query = CRUDHelper.deleteQuery(Tables.SUBJECTS_COMBINATION, userId);

        try {
            databaseService.delete(query);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not delete from database because " + ex.getMessage());
        }
    }

    public static ObservableList<NoteSubject> getNoteSubjects() {
        return FXCollections.unmodifiableObservableList(noteSubjects);
    }

    public static ObservableList<ObjectiveSubject> getObjectiveSubjects() {
        return FXCollections.unmodifiableObservableList(objectiveSubjects);
    }

    public static ObservableList<FavoriteSubject> getFavoriteSubjects() {
        return FXCollections.unmodifiableObservableList(favoriteSubjects);
    }

    public static ObservableList<TheorySubject> getTheorySubjects() {
        return FXCollections.unmodifiableObservableList(theorySubjects);
    }

    public static ObservableList<Subject> getSubjects() {
        return FXCollections.unmodifiableObservableList(subjects);
    }

    public static int getPQSubjectId(int subjectId) {
        for (ObjectiveSubject subject : objectiveSubjects) {
            if (subject.getSubjectId() == subjectId) return subject.getId();
        }
        return 0;
    }

    public static NoteSubject getNoteSubject(int id) {
        for (NoteSubject subject : noteSubjects) {
            if (subject.getId() == id) return subject;
        }
        return null;
    }

    public static Optional<Subject> getSubject(int id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) return Optional.of(subject);
        }
        return Optional.empty();
    }
}
