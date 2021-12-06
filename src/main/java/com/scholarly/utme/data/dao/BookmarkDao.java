package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Bookmark;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookmarkDao {

    private static final String tableName = "bookmarks";

    private static final String idColumn = "id";
    private static final String questionIdColumn = "questionId";
    private static final String subjectIdColumn = "subjectId";
    private static final String yearIdColumn = "yearId";


    private static final ObservableList<Bookmark> bookmarks;

    static {
        bookmarks = FXCollections.observableArrayList();
        updateSubjectsFromDB();
    }


    public static ObservableList<Bookmark> getBookmarks(int subjectId) {
        ObservableList<Bookmark> bookmarks = FXCollections.observableArrayList();

        String query;

        query = "SELECT * FROM " + tableName + " WHERE subjectId = " + subjectId;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new Bookmark(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(questionIdColumn),
                        rs.getInt(yearIdColumn)));
            }


            return bookmarks;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load bookmarks from database ");
            bookmarks.clear();

            return null;
        }
    }

    public static void deleteBookmark(int id) {
        CRUDHelper.delete(tableName, id);
    }

    public static int createBookmark(int questionId, int subjectId, int yearId) {
        int id = (int) CRUDHelper.create(
                tableName,
                new String[]{"questionId", "subjectId", "yearId"},
                new Object[]{questionId, subjectId, yearId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        return id;
    }

    private static void updateSubjectsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            bookmarks.clear();
            while (rs.next()) {
                bookmarks.add(new Bookmark(
                        rs.getInt(idColumn),
                        rs.getInt(questionIdColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Bookmarks from database ");
            bookmarks.clear();
        }
    }

    public static ObservableList<Bookmark> getBookmarks() {
        return FXCollections.unmodifiableObservableList(bookmarks);
    }

    public static Optional<Bookmark> getBookmark(int id) {
        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getId() == id) return Optional.of(bookmark);
        }
        return Optional.empty();
    }

}
