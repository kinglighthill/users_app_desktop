package com.scholarly.utme.data.dao.newDb;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SyllabusSubject;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.SyllabusDatabase;
import com.scholarly.utme.data.util.Tables;
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

public class SyllabusSubjectDao {
    private static final String TAG = "SyllabusSubjectDao ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String genObjectivesColumn = "general_objectives";
    private static final String recTextsColumn = "recommended_texts";
    private static final String subjectIdColumn = "subject_id";
    private static final String orderColumn = "order";
    private static final String titleColumn = "title";

    private static final ObservableList<SyllabusSubject> syllabusSubjects;

    static {
        syllabusSubjects = FXCollections.observableArrayList();
        updateSyllabusSubjectsFromDB();
    }

    private static void updateSyllabusSubjectsFromDB() {
        String query = "SELECT * FROM " + Tables.SYLLABUS_SUBJECTS + " JOIN " + Tables.SUBJECTS + " ON " + Tables.SYLLABUS_SUBJECTS + ".subject_id = " + Tables.SUBJECTS + "._id ORDER BY " + "\"order\"";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            syllabusSubjects.clear();
            while (rs.next()) {
                syllabusSubjects.add(new SyllabusSubject(
                        rs.getInt(idColumn),
                        rs.getString(genObjectivesColumn),
                        rs.getString(recTextsColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getString(titleColumn),
                        rs.getInt(orderColumn)));
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Syllabus Subjects from database because " + e.getMessage());
            syllabusSubjects.clear();
        }
    }

    public static ObservableList<SyllabusSubject> getSyllabusSubjects() {
        return FXCollections.unmodifiableObservableList(syllabusSubjects);
    }

    public static Optional<SyllabusSubject> getSubject(int subject_id) {
        for (SyllabusSubject subject : syllabusSubjects) {
            if (subject.getId() == subject_id) return Optional.of(subject);
        }
        return Optional.empty();
    }

    public static SyllabusSubject getSubjectWithId(int subjectId) {
        for (SyllabusSubject subject : syllabusSubjects) {
            if (subject.getSubjectId() == subjectId) {
                return subject;
            }
        }
        return null;
    }

    public static int getCategorySubjectId(int subjectId) {
        for (SyllabusSubject subject : syllabusSubjects) {
            if (subject.getSubjectId() == subjectId) return subject.getId();
        }
        return 0;
    }
}
