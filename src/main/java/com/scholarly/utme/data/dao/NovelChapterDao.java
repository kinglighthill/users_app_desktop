package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.FreeContent;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class NovelChapterDao {
    private static final String TAG = "NovelChapterDao: ";
    private static final DatabaseService databaseService = new DatabaseService();
    private static Preferences preferences = AppPreferences.getPreferences();

    private static final String idColumn = "_id";
    private static final String positionColumn = "position";
    private static final String titleColumn = "title";
    private static final String descriptionColumn = "description";
    private static final String chapterCategoryIdColumn = "chapter_category_id";
    private static final String novelIdColumn = "novel_id";
    private static final String orderColumn = "order";

    private static final ObservableList<FreeContent> freeContents;

    ObservableList<NovelChapter> novelChapters;
    private static final String userId;

    static {
        userId = preferences.get(PREF_KEY_USER_ID, "");
        freeContents = FXCollections.observableArrayList();
        updateFreeChaptersColumn();
    }

    private void updateNovelChaptersFromDb() {
        novelChapters = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Tables.NOVEL_CHAPTERS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            novelChapters.clear();
            while (rs.next()) {
                novelChapters.add(new NovelChapter(
                        rs.getInt(idColumn),
                        rs.getInt(positionColumn),
                        rs.getString(titleColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(chapterCategoryIdColumn),
                        rs.getInt(novelIdColumn),
                        rs.getInt(orderColumn),
                        false));

            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novel chapters from database because " + e.getMessage());
            novelChapters.clear();
        }
    }

    private static void updateFreeChaptersColumn() {
        String query = "SELECT * FROM " + Tables.FREE_CONTENTS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            freeContents.clear();
            while (rs.next()) {
                freeContents.add(new FreeContent(
                        rs.getInt(idColumn),
                        rs.getInt("objective_subject_id"),
                        rs.getInt("theory_subject_id"),
                        rs.getInt("year_id"),
                        rs.getInt("topic_id"),
                        rs.getInt("chapter_id")));

            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Free Contents from database because " + e.getMessage());
            freeContents.clear();
        }
    }

    public ObservableList<NovelChapter> getNovelChapters() {
        updateNovelChaptersFromDb();
        if (preferences.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false)) {
            for (NovelChapter chapter : novelChapters) {
                chapter.setFree(true);
            }
        } else {
            for (NovelChapter chapter : novelChapters) {
                for (FreeContent content : freeContents) {
                    if (content.getChapterId() == chapter.getId()) {
                        chapter.setFree(true);
                    }
                }
            }
        }
        return novelChapters;
    }
}
