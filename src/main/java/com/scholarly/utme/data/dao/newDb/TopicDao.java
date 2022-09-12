package com.scholarly.utme.data.dao.newDb;


import com.scholarly.utme.data.model.newDb.PQTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.SyllabusDatabase;
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

public class TopicDao {
    private static final String TAG = "TopicDao: ";

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String orderColumn = "order";

    private static final String subjectIdColumn = "subject_id";
    private static final String createdAtColumn = "created_at";


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
                        rs.getString(titleColumn),
                        rs.getInt(orderColumn)));
            }

            return topics;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load topics from database because " + e.getMessage());
            topics.clear();

            return null;
        }

    }

    public static ObservableList<PQTopic> getTopicsForSubject(int subjectId) {
        ObservableList<PQTopic> topics = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.TOPICS + " WHERE subject_id = " + subjectId;

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            topics.clear();
            while (rs.next()) {
                topics.add(new PQTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getString(createdAtColumn)));
            }

//            System.out.println(TAG + "Got topics of size -> " + topics.size());
            return topics;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load topics from database because " + e.getMessage());
            topics.clear();

            return null;
        }

    }

}
