package com.scholarly.utme.data.dao.newDb;


import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.FreeContent;
import com.scholarly.utme.data.model.newDb.NoteTopic;
import com.scholarly.utme.data.model.newDb.PQTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.SyllabusDatabase;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.network.model.Data;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class TopicDao {
    private static final String TAG = "TopicDao: ";

    private static final DatabaseService databaseService = new DatabaseService();
    private static Preferences preferences = AppPreferences.getPreferences();

    private static final String idColumn = "_id";
    private static final String titleColumn = "title";
    private static final String topicIdColumn = "topic_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String orderColumn = "order";

    private static final List<PQTopic> pqTopics;
    private static final ObservableList<NoteTopic> noteTopics;
    private static final ObservableList<FreeContent> freeContents;

    static {
        pqTopics = FXCollections.observableArrayList();
        noteTopics = FXCollections.observableArrayList();
        freeContents = FXCollections.observableArrayList();
        updateTopicsFromDb();
        updateNoteTopicsFromDb();
        updateFreeTopicsColumn();
    }

    private static void updateTopicsFromDb() {
        String query = "SELECT * FROM " + Tables.TOPICS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            pqTopics.clear();
            while (rs.next()) {
                pqTopics.add(new PQTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn)));
            }

            System.out.println(TAG + "Got PQ topics of size -> " + pqTopics.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load topics from database because " + e.getMessage());
            pqTopics.clear();

        }
    }

    private static void updateNoteTopicsFromDb() {
        String query = "SELECT * FROM " + Tables.NOTE_TOPICS + " ORDER BY '" + orderColumn + "'";

        try (ResultSet rs = databaseService.executeQuery(query)) {
            noteTopics.clear();
            while (rs.next()) {
                noteTopics.add(new NoteTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(orderColumn),
                        false));
            }

            System.out.println(TAG + "Got Note topics of size -> " + noteTopics.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Note topics from database because " + e.getMessage());
            noteTopics.clear();

        }
    }

    private static void updateFreeTopicsColumn() {
        String query = "SELECT * FROM " + Tables.FREE_CONTENTS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            freeContents.clear();
            while (rs.next()) {
                freeContents.add(new FreeContent(
                        rs.getInt(idColumn),
                        rs.getInt("subject_id"),
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
    }

    public static ObservableList<PQTopic> getTopicsForSubject(int subjectId) {
        return FXCollections.observableArrayList(
                pqTopics.stream().filter(
                        pqTopic -> pqTopic.getSubjectId() == subjectId).collect(Collectors.toList()
                )
        );

    }

    public static ObservableList<NoteTopic> getNoteTopicsForSubject(int subjectId) {
        String query = "SELECT * FROM " + Tables.NOTE_TOPICS + " WHERE " + subjectIdColumn + " = " + subjectId + " ORDER BY '" + orderColumn + "'";
        ObservableList<NoteTopic> noteTopics = FXCollections.observableArrayList();

        try (ResultSet rs = databaseService.executeQuery(query)) {
            noteTopics.clear();
            while (rs.next()) {
                noteTopics.add(new NoteTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(orderColumn),
                        false));
            }

            System.out.println(TAG + "Got Note topics of size -> " + noteTopics.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Note topics from database because " + e.getMessage());
            noteTopics.clear();

        }

        if (preferences.getBoolean(Constants.PREF_KEY_ACTIVATION_STATE, false)) {
            for (NoteTopic topic : noteTopics) {
                topic.setFree(true);
            }
        } else {
            for (NoteTopic topic : noteTopics) {
                for (FreeContent content : freeContents) {
                    if (content.getTopicId() == topic.getId()) {
                        topic.setFree(true);
                    }
                }
            }
        }
        return noteTopics;
    }

    public static ObservableList<NoteTopic> getNoteTopics() {
        return FXCollections.observableArrayList(noteTopics);

    }

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

}
