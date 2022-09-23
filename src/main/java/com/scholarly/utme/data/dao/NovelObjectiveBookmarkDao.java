package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.ObjectiveBookmark;
import com.scholarly.utme.data.model.novels.NovelObjectiveBookmark;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static final List<NovelObjectiveBookmark> objectiveBookmarks;

    static {
        objectiveBookmarks = FXCollections.observableArrayList();
        updateNovelObjectiveBookmarks();
    }

    private static void updateNovelObjectiveBookmarks() {
        String query = "SELECT * FROM " + Tables.BOOKMARKS_NOVEL_OBJECTIVE_QUESTION;

        try (Connection connection = NewDatabase.connect()) {
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

    public List<NovelObjectiveBookmark> getObjectiveBookmarks() {
        return objectiveBookmarks;
    }

    public List<NovelObjectiveBookmark> getNovelBookmarkWithChapterId(int chapterId) {
        return objectiveBookmarks.stream().filter(novelObjectiveBookmark ->
                novelObjectiveBookmark.getChapterId() == chapterId).collect(Collectors.toList());
    }

}
