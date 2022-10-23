package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectiveBookmarkDao {
    private static final String TAG = "ObjectiveBookmarkDao: ";

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String questionIdColumn = "question_id";
    private static final String createdAtColumn = "created_at";


    public static ObservableList<ObjectiveBookmark> getBookmarks(int subjectId) {
        ObservableList<ObjectiveBookmark> bookmarks = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.BOOKMARKS_OBJECTIVE_QUESTIONS + " WHERE " + subjectIdColumn + " = " + subjectId;

        try {
            Connection connection = DbConnection.getDbConnection();
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new ObjectiveBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(questionIdColumn),
                        rs.getString(createdAtColumn)));
            }

            System.out.println(TAG + "Got bookmarks of length -> " + bookmarks.size() + " for subject with ID -> " + subjectId);

            return bookmarks;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load bookmarks from database because of " + e.getMessage());
            bookmarks.clear();

            return null;
        }
    }

    public static ObservableList<ObjectiveBookmark> getBookmarks() {
        ObservableList<ObjectiveBookmark> bookmarks = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.BOOKMARKS_OBJECTIVE_QUESTIONS;

        try {
            Connection connection = DbConnection.getDbConnection();
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new ObjectiveBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(questionIdColumn),
                        rs.getString(createdAtColumn)));
            }

            System.out.println(TAG + "Got bookmarks of length -> " + bookmarks.size());

            return bookmarks;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load bookmarks from database because of " + e.getMessage());
            bookmarks.clear();

            return null;
        }
    }


    public static int deleteBookmark(int questionId) {
        int sqlResponse = CRUDHelper.delete(Tables.BOOKMARKS_OBJECTIVE_QUESTIONS, questionId);
        if (sqlResponse == 1) {
            System.out.println(TAG + "Bookmark with questionId -> " + questionId + " deleted successfully");
        } else {
            System.out.println(TAG + "Bookmark delete operation unsuccessful");
        }

        return sqlResponse;
    }

    public static int createBookmark(int subjectId, int yearId, int questionId) {
        int id = (int) CRUDHelper.create(
                Tables.BOOKMARKS_OBJECTIVE_QUESTIONS,
                new String[]{"subject_id", "year_id","question_id"},
                new Object[]{subjectId, yearId, questionId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        System.out.println(TAG + "Bookmark created with details [Id -> " + id + ", subject_id -> " + subjectId + ", year_id -> " + yearId + ", question_id -> " + questionId);
        return id;
    }


    public static boolean createTable() {

        String query = "CREATE TABLE IF NOT EXISTS " + Tables.BOOKMARKS_OBJECTIVE_QUESTIONS +
                " (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year_id INTEGER NOT NULL, " +
                "question_id INTEGER NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(subject_id) REFERENCES pq_subjects(_id), " +
                "FOREIGN KEY(year_id) REFERENCES years(_id), " +
                "FOREIGN KEY(question_id) REFERENCES pq_objective_questions(_id), " +
                "UNIQUE(subject_id, year_id, question_id)" +
                ");";

//        System.out.println(TAG + "Create Table Query -> " + query);

        try {
            Connection connection = DbConnection.getDbConnection();

            Statement statement = connection.createStatement();
            statement.execute(query);

            return true;
        } catch (SQLException e) {
            System.out.println(TAG + "Could not create Objective Bookmark Table because " + e.getMessage());
            return false;
        }
    }

}
