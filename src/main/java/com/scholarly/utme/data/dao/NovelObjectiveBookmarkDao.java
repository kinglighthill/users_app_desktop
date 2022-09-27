package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.novels.NovelObjectiveBookmark;
import com.scholarly.utme.data.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NovelObjectiveBookmarkDao {
    private static final String TAG = "NovelObjectiveBookmarkDao: ";

    private static final String idColumn = "_id";
    private static final String novelIdColumn = "novel_id";
    private static final String chapterIdColumn = "chapter_id";
    private static final String questionIdColumn = "question_id";

    private static final ObservableList<NovelObjectiveBookmark> objectiveBookmarks;

    static {
        objectiveBookmarks = FXCollections.observableArrayList();
        updateNovelObjectiveBookmarks();
    }

    private static void updateNovelObjectiveBookmarks() {
        String query = "SELECT * FROM " + Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION;

        try {
            Connection connection = DbConnection.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            objectiveBookmarks.clear();
            while (rs.next()) {
                objectiveBookmarks.add(new NovelObjectiveBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(novelIdColumn),
                        rs.getInt(chapterIdColumn),
                        rs.getInt(questionIdColumn)));
            }

            System.out.println(TAG + "Got novel objective bookmarks of length -> " + objectiveBookmarks.size());

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load bookmarks from database because of " + e.getMessage());
            objectiveBookmarks.clear();

        }
    }

    public static ObservableList<NovelObjectiveBookmark> getNovelBookmarks(int chapterId) {
        ObservableList<NovelObjectiveBookmark> chapterBookmarks = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION + " WHERE chapter_id = " + chapterId;

        try {
            Connection connection = DbConnection.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            chapterBookmarks.clear();
            while (rs.next()) {
                chapterBookmarks.add(new NovelObjectiveBookmark(
                        rs.getInt(idColumn),
                        rs.getInt(novelIdColumn),
                        rs.getInt(chapterIdColumn),
                        rs.getInt(questionIdColumn)));
            }
            return chapterBookmarks;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load chapterBookmarks from database because of " + e.getMessage());
            chapterBookmarks.clear();
            return null;

        }
    }

    public static int deleteBookmark(int questionId) {
        return CRUDHelper.delete(Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION, questionId);
    }

    public static int createBookmark(int novelId, int chapterId, int questionId) {
        int id = (int) CRUDHelper.create(
                Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION,
                new String[]{"novel_id", "chapter_id", "question_id"},
                new Object[]{novelId, chapterId, questionId},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        System.out.println(TAG + "Bookmark created with ID -> " + id + " AND question_id -> " + questionId);
        return id;
    }

    public ObservableList<NovelObjectiveBookmark> getObjectiveBookmarks() {
        return objectiveBookmarks;
    }

    public static ObservableList<NovelObjectiveBookmark> getNovelBookmarkWithChapterId(int chapterId) {
        return FXCollections.observableArrayList(
                objectiveBookmarks.stream().filter(novelObjectiveBookmark ->
                novelObjectiveBookmark.getChapterId() == chapterId).collect(Collectors.toList())
        );
    }


    public static boolean createTable() {

        String query = "CREATE TABLE IF NOT EXISTS " + Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION +
                "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "novel_id INTEGER NOT NULL, " +
                "chapter_id INTEGER NOT NULL, " +
                "question_id INTEGER NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(novel_id) REFERENCES novels(_id), " +
                "FOREIGN KEY(chapter_id) REFERENCES novel_chapters(_id), " +
                "FOREIGN KEY(question_id) REFERENCES novel_objective_questions(_id), " +
                "UNIQUE(novel_id, chapter_id, question_id)" +
                ");";

//        System.out.println(TAG + "Create Table Query -> " + query);

        try {
            Connection connection = DbConnection.getDbConnection();

            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println(TAG + "Could not create Novel Objective Bookmark Table because " + e.getMessage());
            return false;
        }
    }

}
