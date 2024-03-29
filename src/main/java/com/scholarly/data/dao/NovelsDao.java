package com.scholarly.data.dao;

import com.scholarly.data.util.DatabaseService;
import com.scholarly.data.model.novels.*;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private static final String divisionColumn = "division";
    private static final String positionColumn = "position";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";
    private static final String creditIdColumn = "credit_id";

    private static final String genreColumn = "genre";
    private static final String categoryColumn = "category";
    private static final String orderColumn = "order";

    private static final String mCategoryIdColumn = "categoryId";
    private static final String mGenreIdColumn = "genreId";

    private static final ObservableList<Novel> novels;
    private static final ObservableList<NovelGenre> genres;
    private static final ObservableList<NovelCategory> categories;

    private static final ObservableMap<NovelCategoryGenre, List<NovelModel>> genresCategories;

    static {
        novels = FXCollections.observableArrayList();
        genres = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        genresCategories = FXCollections.observableHashMap();

        updateNovelsFromDb();
        updateGenresFromDb();
        updateCategoriesFromDb();
        updateGenresCategoriesFromDb();
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

    private static void updateGenresCategoriesFromDb() {
        String query = """
                WITH category_genre_ids AS (SELECT DISTINCT category_id, genre_id FROM novels),\s
                category_genre AS (SELECT category_genre_ids.category_id AS categoryId, category_genre_ids.genre_id AS genreId, novel_categories.category, novel_genres.genre, novel_categories."order" AS category_order, novel_genres."order" AS genre_order\s
                                FROM category_genre_ids JOIN  novel_categories ON  novel_categories._id = category_genre_ids.category_id JOIN  novel_genres ON  novel_genres._id = category_genre_ids.genre_id\s
                                WHERE novel_categories._id = category_genre_ids.category_id AND novel_genres._id = category_genre_ids.genre_id ORDER BY novel_categories."order", novel_genres."order")
                                SELECT novels._id, novels.image_path, novels.name, novels.summary, novels.about, novels.chapters_count, novels.category_id, novels.genre_id, novels.division_id, novels.position, novels.is_new, novels.available, novels.credit_id, category_genre.categoryId, category_genre.genreId, category_genre.category, category_genre.genre, novel_divisions.division\s
                                FROM category_genre JOIN novels ON category_genre.categoryId = novels.category_id AND category_genre.genreId = novels.genre_id JOIN novel_divisions ON novels.division_id = novel_divisions._id
                                ORDER BY category_genre."category_order", category_genre."genre_order", novels.position;""";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            genresCategories.clear();
            while (rs.next()) {
                int id = rs.getInt(idColumn);
                String imagePath = rs.getString(imagePathColumn);
                String name = rs.getString(nameColumn);
                String summary = rs.getString(summaryColumn);
                String about = rs.getString(aboutColumn);
                int chaptersCount = rs.getInt(chaptersCountColumn);
                int categoryId = rs.getInt(categoryIdColumn);
                int genreId = rs.getInt(genreIdColumn);
                int divisionId = rs.getInt(divisionIdColumn);
                int position = rs.getInt(positionColumn);
                int isNew = rs.getInt(isNewColumn);
                int available = rs.getInt(availableColumn);
                int creditId = rs.getInt(creditIdColumn);
                String division = rs.getString(divisionColumn);

                int mCategoryId = rs.getInt(mCategoryIdColumn);
                int mGenreId = rs.getInt(mGenreIdColumn);
                String category = rs.getString(categoryColumn);
                String genre = rs.getString(genreColumn);

                NovelCategoryGenre novelCategoryGenre = new NovelCategoryGenre(mCategoryId, mGenreId, category, genre);
                NovelModel novelModel = new NovelModel(
                        new Novel(id, imagePath, name, summary, about, chaptersCount, genreId, categoryId, divisionId, position, isNew, available, creditId),
                        division
                );

                List<NovelModel> novelModels = genresCategories.get(novelCategoryGenre);
                if (novelModels == null) {
                    novelModels = new ArrayList<>();
                }
                novelModels.add(novelModel);
                genresCategories.put(novelCategoryGenre, novelModels);
            }

            System.out.println(TAG + "Got Category Genres of size -> " + genresCategories.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Categories from database because " + e.getMessage());
            categories.clear();
        }
    }

    public static NovelModel getNovel(int novelId) {
        String query = "SELECT * FROM " + Tables.NOVELS + " WHERE " + idColumn + " = " + novelId;

        Novel novel;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            novel = new Novel(
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
                    rs.getInt(creditIdColumn)
            );

            String divisionQuery = "SELECT * FROM " + Tables.NOVEL_DIVISIONS + " WHERE " + idColumn + " = " + novel.getDivisionId();

            String division = "";

            try(ResultSet resultSet = databaseService.executeQuery(divisionQuery)) {
                division = resultSet.getString(divisionColumn);
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ": Could not find Novel Division from database because " + e.getMessage());
            }

            //            System.out.println(TAG + "Got NovelModel -> " + Helper.toString(novelModel));

            return new NovelModel(novel, division);

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not find Novel from database because " + e.getMessage());
            return null;
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

    public static ObservableMap<NovelCategoryGenre, List<NovelModel>> getGenresCategories() {
        return FXCollections.unmodifiableObservableMap(genresCategories);
    }
}
