package com.scholarly.utme.data.dao;


import com.scholarly.utme.data.model.Topic;
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

public class TopicDao {

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";


    public static ObservableList<Topic> getTopics(String tableName) {
        ObservableList<Topic> topics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            topics.clear();
            while (rs.next()) {
                topics.add(new Topic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn)));
            }


            return topics;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load topics from database ");
            topics.clear();

            return null;
        }
    }
}
