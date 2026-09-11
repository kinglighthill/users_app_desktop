package com.scholarly.utme.data.dao.newDb;

import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import com.scholarly.utme.data.util.SyllabusDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyllabusTopicDao {

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String categoryColumn = "category_id";
    private static final String orderColumn = "order";

    public static ObservableList<SyllabusTopic> getSyllabusTopics(String tableName) {
        ObservableList<SyllabusTopic> topics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try(Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            topics.clear();
            while (rs.next()){
                topics.add(new SyllabusTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(categoryColumn),
                        rs.getInt(orderColumn)));
            }
            //System.out.println("Got category from DB" + tableName + ": " + categories);
            return topics;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load categories from database ");
            topics.clear();

            return null;
        }
    }

    public static ObservableList<SyllabusTopic> getTopicsForCategory(String tableName, int categoryId) {
        ObservableList<SyllabusTopic> topics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName + " WHERE " + categoryColumn + " = " + categoryId;

        try(Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            topics.clear();
            while (rs.next()){
                topics.add(new SyllabusTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(categoryColumn),
                        rs.getInt(orderColumn)));
            }
            System.out.println("Got topics " + topics + " from DB " + tableName + ": with category id " + categoryId);
            return topics;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load categories from database ");
            topics.clear();

            return null;
        }
    }
}
