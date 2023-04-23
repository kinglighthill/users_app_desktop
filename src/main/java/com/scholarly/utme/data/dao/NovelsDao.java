package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelCategory;
import com.scholarly.utme.data.model.novels.NovelGenre;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NovelsDao {
    private static final String TAG = "NovelsDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String imagePathColumn = "image_path";
    private static final String nameColumn = "name";
    private static final String summaryColumn = "summary";
    private static final String aboutColumn = "about";
    private static final String chaptersCountColumn = "chapters_count";
    private static final String genreIdColumn = "genre_id";
    private static final String typeIdColumn = "type_id";
    private static final String categoryIdColumn = "category_id";
    private static final String divisionIdColumn = "division_id";
    private static final String positionColumn = "position";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";
    private static final String creditIdColumn = "credit_id";

    private static final String genreColumn = "genre";
    private static final String categoryColumn = "category";
    private static final String orderColumn = "order";

    private static final ObservableList<Novel> novels;
    private static final ObservableList<NovelGenre> genres;
    private static final ObservableList<NovelCategory> categories;

    static {
        novels = FXCollections.observableArrayList();
        genres = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        updateNovelsFromDb();
        updateGenresFromDb();
        updateCategoriesFromDb();
    }

    private static void updateNovelsFromDb() {
        String query = "SELECT * FROM " + Tables.NOVELS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            novels.clear();
            while (rs.next()) {
                novels.add(new Novel(
                        rs.getInt(idColumn),
                        rs.getString(imagePathColumn),
                        rs.getString(nameColumn),
                        rs.getString(summaryColumn),
                        rs.getString(aboutColumn),
                        rs.getInt(chaptersCountColumn),
                        rs.getInt(genreIdColumn),
                        rs.getInt(categoryIdColumn),
                        rs.getInt(divisionIdColumn),
                        rs.getInt(positionColumn),
                        rs.getInt(isNewColumn),
                        rs.getInt(availableColumn),
                        rs.getInt(creditIdColumn)));
            }

            System.out.println(TAG + "Got novels of size -> " + novels.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novels from database because " + e.getMessage());
            novels.clear();
        }
    }

    private static void updateGenresFromDb() {
        String query = "SELECT * FROM " + Tables.NOVEL_GENRES + " ORDER BY \"" + orderColumn + "\"";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            genres.clear();
            while (rs.next()) {
                genres.add(new NovelGenre(
                        rs.getInt(idColumn),
                        rs.getString(genreColumn),
                        rs.getString(orderColumn)));
            }

            System.out.println(TAG + "Got genres of size -> " + genres.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Genres from database because " + e.getMessage());
            genres.clear();
        }
    }

    private static void updateCategoriesFromDb() {
        String query = "SELECT * FROM " + Tables.NOVEL_CATEGORIES + " ORDER BY \"" + orderColumn + "\"";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            categories.clear();
            while (rs.next()) {
                categories.add(new NovelCategory(
                        rs.getInt(idColumn),
                        rs.getString(categoryColumn),
                        rs.getString(orderColumn)));
            }

            System.out.println(TAG + "Got categories of size -> " + categories.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Categories from database because " + e.getMessage());
            categories.clear();
        }
    }

    public static ObservableList<Novel> getNovels() {
        return FXCollections.unmodifiableObservableList(novels);
    }

    public static ObservableList<NovelGenre> getGenres() {
        return FXCollections.unmodifiableObservableList(genres);
    }

    public static ObservableList<NovelCategory> getCategories() {
        return FXCollections.unmodifiableObservableList(categories);
    }

}
