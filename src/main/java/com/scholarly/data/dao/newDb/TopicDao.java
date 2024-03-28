package com.scholarly.data.dao.newDb;


import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.FreeContent;
import com.scholarly.data.model.newDb.NoteTopic;
import com.scholarly.data.model.newDb.PQTopic;
import com.scholarly.data.util.Tables;
import com.scholarly.util.PreferencesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.scholarly.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class TopicDao {
    private static final String TAG = "TopicDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

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

        long start = System.currentTimeMillis();

        try (ResultSet rs = databaseService.executeQuery(query)) {
            pqTopics.clear();
            while (rs.next()) {
                pqTopics.add(new PQTopic(
                        rs.getInt(idColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn)));
            }

            long end = System.currentTimeMillis();

            System.out.println(TAG + "Got PQ topics of size -> " + pqTopics.size() + " in -> " + (end - start)+"ms");

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
                        rs.getInt("objective_subject_id"),
                        rs.getInt("theory_subject_id"),
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

    public static ObservableList<PQTopic> getPQTopicsForSubjectAndYear(int subjectId, int yearId) {
        String query = "SELECT DISTINCT topic_id, topics.title, pq_objective_questions.subject_id FROM pq_objective_questions JOIN topics ON topics._id = pq_objective_questions.topic_id WHERE pq_objective_questions.subject_id = " + subjectId + " AND year_id = " + yearId;
//        System.out.println(TAG + "Get Topics with Subject and Year Query -> " + query);
        ObservableList<PQTopic> topics = FXCollections.observableArrayList();

        try (ResultSet rs = databaseService.executeQuery(query)) {

            while (rs.next()) {
                topics.add(new PQTopic(
                        rs.getInt(topicIdColumn),
                        rs.getString(titleColumn),
                        rs.getInt(subjectIdColumn)));
            }

            long end = System.currentTimeMillis();

            return topics;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not get topics for subject and year from database because " + e.getMessage());

            return null;

        }

    }

    public static ObservableList<NoteTopic> getNoteTopicsForSubject(int subjectId) {
//        String query = "SELECT DISTINCT note_topics._id, note_topics.title, note_topics.topic_id, note_topics.subject_id, note_topics.'order' FROM note_topics JOIN note_sections ON note_topics.topic_id = note_sections.topic_id WHERE note_topics.subject_id = " + subjectId + " ORDER BY 'order'";
        String query2 = "SELECT DISTINCT " + Tables.NOTE_TOPICS + "." + idColumn + ", " + Tables.NOTE_TOPICS + "." + titleColumn + ", " +
                Tables.NOTE_TOPICS + "." + topicIdColumn + ", " + Tables.NOTE_TOPICS + "." + subjectIdColumn + ", " + Tables.NOTE_TOPICS + ".'" + orderColumn +
                "' FROM " + Tables.NOTE_TOPICS + " INNER JOIN " + Tables.NOTE_SECTIONS + " ON " + Tables.NOTE_SECTIONS + "." + topicIdColumn + " = " + Tables.NOTE_TOPICS + "." + idColumn + " WHERE " + Tables.NOTE_TOPICS + "." + subjectIdColumn + " = " + subjectId + " ORDER BY '" + orderColumn + "'";
//        System.out.println(TAG + "NoteTopicsForSubject Query -> " + query);
//        System.out.println(TAG + "NoteTopicsForSubject Query 2 -> " + query2);
        ObservableList<NoteTopic> noteTopics = FXCollections.observableArrayList();

        try (ResultSet rs = databaseService.executeQuery(query2)) {
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

        String userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        if (PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false)) {
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

    public static NoteTopic getTopic(int id) {
        for (NoteTopic topic : noteTopics) {
            if (topic.getId() == id) return topic;
        }
        return null;
    }

}
