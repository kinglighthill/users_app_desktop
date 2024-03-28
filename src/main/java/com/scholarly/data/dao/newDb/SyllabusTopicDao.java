package com.scholarly.data.dao.newDb;

import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.newDb.SyllabusTopic;
import com.scholarly.data.util.SyllabusDatabase;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SyllabusTopicDao {
    private static final String TAG = "SyllabusTopicDao: ";

    private static final DatabaseService databaseService = new DatabaseService();
    private static final String idColumn = "_id";
    private static final String sectionColumn = "section_id";
    private static final String categoryColumn = "category_id";
    private static final String orderColumn = "order";

    private static ObservableList<SyllabusTopic> topics;

    static {
        topics = FXCollections.observableArrayList();
        updateSyllabusTopicsFromDb();
    }

    private static void updateSyllabusTopicsFromDb() {

        String query = "SELECT * FROM " + Tables.SYLLABUS_TOPICS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            topics.clear();
            while (rs.next()){
                topics.add(new SyllabusTopic(
                        rs.getInt(idColumn),
                        rs.getInt(sectionColumn),
                        rs.getInt(categoryColumn),
                        rs.getInt(orderColumn)));
            }
            //System.out.println("Got category from DB" + tableName + ": " + categories);

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load syllabus topics from database because " + e.getMessage());
            topics.clear();
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
                        rs.getInt(sectionColumn),
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

    public ObservableList<SyllabusTopic> getSyllabusTopics() {
        return FXCollections.unmodifiableObservableList(topics);
    }

    public static ObservableList<SyllabusTopic> getTopicsForCategory(int categoryId) {
        return FXCollections.observableArrayList(topics.stream().filter(syllabusTopic ->
                syllabusTopic.getCategoryId() == categoryId
        ).collect(Collectors.toList()));
    }
}
