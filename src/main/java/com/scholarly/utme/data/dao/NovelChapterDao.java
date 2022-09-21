package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NovelChapterDao {
    private static final String TAG = "NovelChapterDao: ";

    private static final String idColumn = "_id";
    private static final String positionColumn = "position";
    private static final String titleColumn = "title";
    private static final String descriptionColumn = "description";
    private static final String detailsColumn = "details";
    private static final String chapterCategoryIdColumn = "chapter_category_id";
    private static final String isReadColumn = "is_read";
    private static final String novelIdColumn = "novel_id";

    private static final ObservableList<NovelChapter> novelChapters;

    static {
        novelChapters = FXCollections.observableArrayList();
        updateNovelsFromDb();
    }

    private static void updateNovelsFromDb() {

        String query = "SELECT * FROM " + Tables.NOVEL_CHAPTERS;

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            novelChapters.clear();
            while (rs.next()) {
                novelChapters.add(new NovelChapter(
                        rs.getInt(idColumn),
                        rs.getInt(positionColumn),
                        rs.getString(titleColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(chapterCategoryIdColumn),
                        rs.getInt(novelIdColumn)));

            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novel chapters from database because " + e.getMessage());
            novelChapters.clear();
        }
    }

    public static ObservableList<NovelChapter> getNovelChapters() {
        return FXCollections.unmodifiableObservableList(novelChapters);
    }
}
