package com.scholarly.utme.data.dao.newDb;

import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.data.util.SyllabusDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyllabusCategoryDao {

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String orderColumn = "order";


    public static ObservableList<SyllabusCategory> getCategories(String tableName) {
        ObservableList<SyllabusCategory> categories = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try(Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            categories.clear();
            while (rs.next()){
                categories.add(new SyllabusCategory(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
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
}
