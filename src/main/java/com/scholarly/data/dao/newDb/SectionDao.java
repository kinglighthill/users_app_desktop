package com.scholarly.data.dao.newDb;


import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.newDb.NoteLastSession;
import com.scholarly.data.model.newDb.NoteSection;
import com.scholarly.data.model.newDb.Section;
import com.scholarly.data.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SectionDao {
    private static final String TAG = "SectionDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String topicIdColumn = "topic_id";
    private static final String subtopicIdColumn = "sub_topic_id";
    private static final String contentColumn = "content";
    private static final String parentSectionIdColumn = "parent_section_id";
    private static final String mainSectionOrderColumn = "main_section_order";
    private static final String childSectionOrderColumn = "child_section_order";
    private static final String contentViewTypeColumn = "content_view_type_id";
    private static final String sectionIdColumn = "section_id";
    private static final String sectionTitleColumn = "section_title";
    private static final String userIdColumn = "uid";

    private static final ObservableList<NoteSection> noteSections;

    static {
        noteSections = FXCollections.observableArrayList();

    }

    public static ObservableList<NoteSection> getNoteSectionsWithTopicId(int topicId) {
        ObservableList<NoteSection> noteSections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.NOTE_SECTIONS + " WHERE topic_id = " + topicId + " ORDER BY " + idColumn;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            noteSections.clear();
            while (rs.next()) {
                NoteSection section = new NoteSection(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(subtopicIdColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeColumn),
                        new JSONObject()
//                        rs.getObject(contentColumn, JSONObject.class)
                );

                noteSections.add(section);
            }
//            System.out.println(TAG + "Got Note sections for topic Id " + topicId + " -> " + Helper.toString(noteSections));
            return noteSections;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load note sections from database because " + e.getMessage());
            noteSections.clear();

            return null;
        }

    }

    public static NoteSection getNoteSectionWithId(int sectionId) {
        NoteSection section = null;

        String query = "SELECT * FROM " + Tables.NOTE_SECTIONS + " WHERE " + idColumn + " = " + sectionId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            while (rs.next()) {
                section = new NoteSection(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(subtopicIdColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeColumn),
                        new JSONObject()
//                        rs.getObject(contentColumn, JSONObject.class)
                );
            }
            return section;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not get section from database because " + e.getMessage());

            return null;
        }
    }

    public static int insertLastSection(NoteLastSession section) {
        String query = CRUDHelper.insertOrReplaceQuery(
                Tables.NOTE_LAST_SESSION,
                new String[]{"_id", "section_id","section_title", "uid"},
                new Object[]{section.getId(), section.getSectionId(), section.getSectionTitle(), section.getUserId()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});

        System.out.println(TAG + "InsertLastSection SQL Query -> " + query);

        try {
            return (int) databaseService.executeUpdate(query);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not insert item to database because " + ex.getMessage());
            return -1;
        }

    }

    public static NoteLastSession retrieveLastSession(String userId) {
        NoteLastSession lastSection = null;
        String query = "SELECT * FROM " + Tables.NOTE_LAST_SESSION + " WHERE " + userIdColumn + " = '" + userId + "'";

        try(ResultSet rs = databaseService.executeQuery(query)) {
            while (rs.next()) {
                lastSection = new NoteLastSession(
                        rs.getInt(idColumn),
                        rs.getInt(sectionIdColumn),
                        rs.getString(sectionTitleColumn),
                        rs.getString(userIdColumn));
            }
            return lastSection;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load last session from database because " + e.getMessage());

            return null;
        }
    }

    public static ObservableList<Section> getSections(String tableName, int sectionId) {
        List<Section> noteViews = new ArrayList<>();

        String query1 = "SELECT * FROM " + tableName + " WHERE _id = " + sectionId;

        String query2 = "SELECT * FROM " + tableName + " WHERE parent_section_id = " + sectionId;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query1);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Section section = new Section(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeColumn)
                );

                noteViews.add(section);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub topics from database ");
            noteViews.clear();

            return null;
        }

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query2);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Section section = new Section(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeColumn)
                );

                noteViews.add(section);
            }
            return FXCollections.observableList(noteViews);
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub topics from database ");
            noteViews.clear();

            return null;
        }
    }

}
