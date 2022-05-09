package com.scholarly.utme.data.dao.newDb;


import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.util.SyllabusDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubTopicDao {

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String orderColumn = "order";
    private static final String topicColumn = "topic_id";
    private static final String sectionColumn = "section_id";


    public static ObservableList<SubTopic> getSubTopics(String tableName) {
        ObservableList<SubTopic> subTopics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            subTopics.clear();
            while (rs.next()) {
                subTopics.add(new SubTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(orderColumn),
                        rs.getInt(topicColumn),
                        rs.getInt(sectionColumn)));
            }

            return subTopics;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub topics from database ");
            subTopics.clear();

            return null;
        }
    }

    public static ObservableList<SubTopic> getSubTopicsWithTopicId(String tableName, int topicId) {
        ObservableList<SubTopic> subTopics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName + " WHERE topic_id = " + topicId;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            subTopics.clear();
            while (rs.next()) {
                subTopics.add(new SubTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(orderColumn),
                        rs.getInt(topicColumn),
                        rs.getInt(sectionColumn)));
            }


            return subTopics;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub topics from database ");
            subTopics.clear();

            return null;
        }
    }

}
