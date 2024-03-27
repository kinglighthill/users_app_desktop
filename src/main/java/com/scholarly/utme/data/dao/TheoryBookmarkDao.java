package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.TheoryBookmark;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheoryBookmarkDao {
    private static final String TAG = "TheoryBookmarkDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String questionIdColumn = "question_id";
    private static final String createdAtColumn = "created_at";

    public static ObservableList<TheoryBookmark> getBookmarks(int subjectId) {
        ObservableList<TheoryBookmark> bookmarks = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.BOOKMARKS_THEORY_QUESTIONS + " WHERE " + subjectIdColumn + " = " + subjectId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new TheoryBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(questionIdColumn),
                        rs.getString(createdAtColumn)));
            }

            System.out.println(TAG + "Got Theory bookmarks of length -> " + bookmarks.size() + " for subject with ID -> " + subjectId);

            return bookmarks;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load bookmarks from database because of " + e.getMessage());
            bookmarks.clear();

            return null;
        }
    }

    public static int deleteBookmark(int questionId) {
        int sqlResponse = CRUDHelper.delete(Tables.BOOKMARKS_THEORY_QUESTIONS, questionId);
        if (sqlResponse == 1) {
            System.out.println(TAG + "Bookmark with questionId -> " + questionId + " deleted successfully");
        } else {
            System.out.println(TAG + "Bookmark delete operation unsuccessful");
        }
        return sqlResponse;
    }

    public static int createBookmark(int subjectId, int yearId, int questionId) {
        int id = (int) CRUDHelper.create(
                Tables.BOOKMARKS_THEORY_QUESTIONS,
                new String[]{"subject_id", "year_id","question_id"},
                new Object[]{subjectId, yearId, questionId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        System.out.println(TAG + "Bookmark created with details [ id -> " + id + ", subject_id -> " + subjectId + ", year_id -> " + yearId + ", question_id -> " + questionId + " ]");
        return id;
    }


    public static boolean createTable() {

        String query = "CREATE TABLE IF NOT EXISTS " + Tables.BOOKMARKS_THEORY_QUESTIONS +
                "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year_id INTEGER NOT NULL, " +
                "question_id INTEGER NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(subject_id) REFERENCES pq_subjects(_id), " +
                "FOREIGN KEY(year_id) REFERENCES years(_id), " +
                "FOREIGN KEY(question_id) REFERENCES pq_theory_questions(_id), " +
                "UNIQUE(subject_id, year_id, question_id)" +
                ");";

//        System.out.println(TAG + "TheoryBookmarks Create Table Query -> " + query);

        try {
            Connection connection = DbConnection.getDbConnection();

            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println(TAG + "Could not create Theory Bookmark Table because " + e.getMessage());
            return false;
        }
    }

}
