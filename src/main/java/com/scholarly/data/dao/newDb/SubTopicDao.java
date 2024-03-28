package com.scholarly.data.dao.newDb;


import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.MediaSubTopic;
import com.scholarly.data.model.newDb.NoteSubTopic;
import com.scholarly.data.model.newDb.SubTopic;
import com.scholarly.data.util.SyllabusDatabase;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SubTopicDao {
    private static final String TAG = "SubTopicDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String orderColumn = "order";
    private static final String topicIdColumn = "topic_id";
    private static final String sectionColumn = "section_id";

    private static final List<NoteSubTopic> noteSubTopics;

    static {
        noteSubTopics = FXCollections.observableArrayList();
        updateNoteSubTopicsFromDb();
    }

    public static void updateNoteSubTopicsFromDb() {
        String query = "SELECT * FROM " + Tables.NOTE_SUB_TOPICS;

        long start = System.currentTimeMillis();

        try (ResultSet rs = databaseService.executeQuery(query)) {
            noteSubTopics.clear();
            while (rs.next()) {
                noteSubTopics.add(new NoteSubTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(orderColumn)));
            }

            long end = System.currentTimeMillis();

            System.out.println(TAG + "Got Note sub topics of size -> " + noteSubTopics.size() + " in -> " + (end - start)+"ms");

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Note sub topics from database because " + e.getMessage());
            noteSubTopics.clear();

        }
    }

    public static ObservableList<SubTopic> getNoteSubTopics(String tableName) {
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
                        rs.getInt(topicIdColumn),
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
                        rs.getInt(topicIdColumn),
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

    public static ObservableList<MediaSubTopic> getSubTopicsWithTopicId(String tableName, int topicId) {
        ObservableList<MediaSubTopic> subTopics = FXCollections.observableArrayList();

        String query = "SELECT * FROM note_" + tableName + "_sub_topics WHERE topic_id = " + topicId;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            subTopics.clear();
            while (rs.next()) {
                subTopics.add(new MediaSubTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(orderColumn),
                        rs.getInt(topicIdColumn),
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

    public static ObservableList<NoteSubTopic> getSubTopicsForTopic(int topicId) {
        return FXCollections.observableArrayList(
                noteSubTopics.stream().filter(
                        subTopic -> subTopic.getTopicId() == topicId).collect(Collectors.toList()
                )
        );

    }

}
