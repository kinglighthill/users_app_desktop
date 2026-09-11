package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.FreeContent;
import com.scholarly.utme.data.model.newDb.NovelLastSession;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.util.CRUDHelper;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.util.PreferencesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class NovelChapterDao {
    private static final String TAG = "NovelChapterDao: ";
    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String positionColumn = "position";
    private static final String titleColumn = "title";
    private static final String descriptionColumn = "description";
    private static final String chapterCategoryIdColumn = "chapter_category_id";
    private static final String novelIdColumn = "novel_id";
    private static final String orderColumn = "order";

    private static final String divisionColumn = "division";
    private static final String chapterCategoryColumn = "chapter_category";

    private static final String chapterIdColumn = "chapter_id";
    private static final String chapterTitleColumn = "chapter_title";
    private static final String userIdColumn = "uid";

    private static final ObservableList<FreeContent> freeContents = FXCollections.observableArrayList();

    ObservableList<NovelChapter> novelChapters;
    private final String userId;

    public NovelChapterDao() {
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        updateFreeChaptersColumn();
    }

    private void updateNovelChaptersFromDb() {
        novelChapters = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Tables.NOVEL_CHAPTERS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            novelChapters.clear();
            while (rs.next()) {
                NovelChapter novelChapter = new NovelChapter(
                        rs.getInt(idColumn),
                        rs.getInt(positionColumn),
                        rs.getString(titleColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(chapterCategoryIdColumn),
                        rs.getInt(novelIdColumn),
                        rs.getInt(orderColumn),
                        false
                );
                novelChapter.setChapterHeading(
                        getChapterHeading(novelChapter)
                );
                novelChapters.add(novelChapter);
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

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (ResultSet rs = databaseService.executeQuery(query)) {
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
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private String getNovelDivision(int novelId) {
        String query = "SELECT novel_divisions.division FROM novels  INNER JOIN novel_divisions ON novel_divisions._id = novels.division_id WHERE novels._id = " + novelId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            rs.next();
            return rs.getString(divisionColumn);
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novel division from database because " + e.getMessage());
            return "";
        }
    }

    private String getNovelChapterCategory(int categoryId) {
        String query = "SELECT chapter_category FROM novel_chapters_categories WHERE _id = " + categoryId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            rs.next();
            return rs.getString(chapterCategoryColumn);
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novel division from database because " + e.getMessage());
            return "";
        }
    }

    public ObservableList<NovelChapter> getNovelChapters() {
        updateNovelChaptersFromDb();
        boolean isActivated = PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false);
        if (isActivated) {
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

    private String getChapterHeading(NovelChapter chapter) {
        String novelDivision = getNovelDivision(chapter.getNovelId());

        String heading;

        if (chapter.getPosition() == 0) {
            heading = chapter.getTitle();
        } else {
            String chapterTitle;
            if (chapter.getTitle() == null) {
                chapterTitle = "";
            } else {
                chapterTitle = ": " + chapter.getTitle();
            }

            if (chapter.getChapterCategoryId() != 0) {
                String novelChapterCategory = getNovelChapterCategory(chapter.getChapterCategoryId());

                String novelDivTxt;
                if (!Objects.equals(novelDivision, "Act")) {
                    novelDivTxt = novelChapterCategory + ", Chapter " + chapter.getPosition();
                } else {
                    novelDivTxt = novelChapterCategory + ", Scene " + chapter.getPosition();
                }

                heading = novelDivTxt + chapterTitle;
            } else {
                heading = novelDivision + " " + chapter.getPosition() + " " + chapterTitle;
            }
        }

        return heading;
    }

    public static int insertLastSection(NovelLastSession chapter) {
        String query = CRUDHelper.insertOrReplaceQuery(
                Tables.NOVEL_LAST_SESSION,
                new String[]{"_id", "chapter_id","chapter_title", "uid"},
                new Object[]{chapter.getId(), chapter.getChapterId(), chapter.getChapterTitle(), chapter.getUserId()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});

        try {
            return (int) databaseService.executeUpdate(query);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not insert item to database because " + ex.getMessage());
            return -1;
        }

    }

    public static NovelLastSession retrieveLastSession(String userId) {
        NovelLastSession lastSection = null;
        String query = "SELECT * FROM " + Tables.NOVEL_LAST_SESSION + " WHERE " + userIdColumn + " = '" + userId + "'";

        try(ResultSet rs = databaseService.executeQuery(query)) {
            while (rs.next()) {
                lastSection = new NovelLastSession(
                        rs.getInt(idColumn),
                        rs.getInt(chapterIdColumn),
                        rs.getString(chapterTitleColumn),
                        rs.getString(userIdColumn));
            }
            return lastSection;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load last session from database because " + e.getMessage());

            return null;
        }
    }

}
