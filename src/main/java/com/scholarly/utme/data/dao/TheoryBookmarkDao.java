package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.TheoryBookmark;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheoryBookmarkDao {
    private static final String TAG = "TheoryBookmarkDao: ";

    private static final String tableName = "question_bookmarks";

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String questionIdColumn = "question_id";
    private static final String createdAtColumn = "created_at";

    private static final String BOOKMARKS_THEORY_QUESTION = "bookmarks_theory_question";
    private static final String ID_COLUMN = "_id";


    private static final ObservableList<TheoryBookmark> bookmarks;

    static {
        System.out.println(TAG + "static initializer called");
        bookmarks = FXCollections.observableArrayList();
//        updateBookmarksFromDB();
    }


    public static ObservableList<TheoryBookmark> getBookmarks(int subjectId) {

        String query = "SELECT * FROM " + BOOKMARKS_THEORY_QUESTION + " WHERE " + subjectIdColumn + " = " + subjectId;

        try (Connection connection = Database.connect()) {
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new TheoryBookmark(
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

    public static ObservableList<TheoryBookmark> getBookmarks() {
        String query = "SELECT * FROM " + BOOKMARKS_THEORY_QUESTION;

        try (Connection connection = Database.connect()) {
            System.out.println(TAG + "Connection object -> " + connection);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new TheoryBookmark(
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

    private static void updateBookmarksFromDB() {

        String query = "SELECT * FROM " + BOOKMARKS_THEORY_QUESTION;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new TheoryBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(questionIdColumn),
                        rs.getString(createdAtColumn)));
            }
            System.out.println(TAG + "Bookmarks -> " + bookmarks);
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load TheoryBookmarks from database because " + e.getMessage());
            bookmarks.clear();
        }
    }


    public static int deleteBookmark(int subjectId, int questionId) {
        int sqlResponse = CRUDHelper.delete(BOOKMARKS_THEORY_QUESTION, subjectId, questionId);
        if (sqlResponse == 1) {
            System.out.println(TAG + "Bookmark with subjectId -> " + subjectId + " and " + " questionId -> " + questionId + " deleted successfully");
        } else {
            System.out.println(TAG + "Bookmark delete operation unsuccessful");
        }

        return sqlResponse;
    }

    public static int createBookmark(int subjectId, int yearId, int questionId) {
        int id = (int) CRUDHelper.create(
                BOOKMARKS_THEORY_QUESTION,
                new String[]{"subject_id", "year_id","question_id"},
                new Object[]{subjectId, yearId, questionId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        System.out.println(TAG + "Bookmark created with details [ id -> " + id + ", subject_id -> " + subjectId + ", year_id -> " + yearId + ", question_id -> " + questionId + " ]");
        return id;
    }

    public static Optional<TheoryBookmark> getBookmark(int id) {
        for (TheoryBookmark bookmark : bookmarks) {
            if (bookmark.getId() == id) return Optional.of(bookmark);
        }
        return Optional.empty();
    }

    public static boolean checkTable() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='bookmarks_theory_question'";

        try (Connection conn = Database.connect()) {
            // create a new table
            if (conn != null) {
                Statement statement = conn.createStatement();
                boolean result = statement.execute(sql);
                System.out.println("Theory Bookmark Table Query available -> " + result);
                return result;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }


    public static boolean createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS " + BOOKMARKS_THEORY_QUESTION +
                " (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "year_id INTEGER NOT NULL, " +
                "question_id INTEGER NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(subject_id) REFERENCES subjects(_id), " +
                "FOREIGN KEY(year_id) REFERENCES years(_id), " +
                "UNIQUE(subject_id, year_id, question_id) " +
                ");";

        try (Connection conn = Database.connect()) {
            // create a new table
            if (conn != null) {
                Statement statement = conn.createStatement();
                statement.execute(sql);
            }
            System.out.println(TAG + "Theory Bookmark Table created successfully");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
