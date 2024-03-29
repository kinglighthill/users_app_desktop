package com.scholarly.data.dao.newDb;

import com.scholarly.data.util.DatabaseService;
import com.scholarly.data.model.newDb.SyllabusCategory;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyllabusCategoryDao {
    private static final String TAG = "SyllabusCategoryDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String subjectIdColumn = "subject_id";
    private static final String orderColumn = "order";

    private static ObservableList<SyllabusCategory> categories;

    static {
        categories = FXCollections.observableArrayList();
        updateCategoriesFromDb();
    }

    private static void updateCategoriesFromDb() {

        String query = "SELECT * FROM " + Tables.SYLLABUS_CATEGORIES;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            categories.clear();
            while (rs.next()){
                categories.add(new SyllabusCategory(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(orderColumn)));
            }
            //System.out.println("Got category from DB" + tableName + ": " + categories);

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load categories from database ");
            categories.clear();

        }
    }

    public static ObservableList<SyllabusCategory> getCategories(int subjectId) {
        ObservableList<SyllabusCategory> categories = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.SYLLABUS_CATEGORIES + " WHERE " + subjectIdColumn + " = " + subjectId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            categories.clear();
            while (rs.next()){
                categories.add(new SyllabusCategory(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(orderColumn)));
            }
            //System.out.println("Got category from DB" + tableName + ": " + categories);
            return categories;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load categories from database ");
            categories.clear();

            return null;
        }
    }

    public static ObservableList<SyllabusCategory> getCategories() {
        return FXCollections.unmodifiableObservableList(categories);
    }
}
