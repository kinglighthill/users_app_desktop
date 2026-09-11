package com.scholarly.utme.data.dao.newDb;


import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.newDb.NoteSection;
import com.scholarly.utme.data.model.newDb.NoteSubject;
import com.scholarly.utme.data.model.newDb.Section;
import com.scholarly.utme.data.util.SyllabusDatabase;
import com.scholarly.utme.data.util.Table;
import com.scholarly.utme.data.util.Tables;
import com.scholarly.utme.util.Helper;
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

    private static final ObservableList<NoteSection> noteSections;

    static {
        noteSections = FXCollections.observableArrayList();

    }

    public static ObservableList<NoteSection> getNoteSectionsWithTopicId(int topicId) {
        ObservableList<NoteSection> noteSections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.NOTE_SECTIONS + " WHERE topic_id = " + topicId;

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
                        rs.getInt(contentViewTypeColumn)
                );

                noteSections.add(section);
            }
            System.out.println(TAG + "Got Note sections for topic Id " + topicId + " -> " + Helper.toString(noteSections));
            return noteSections;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub topics from database ");
            noteSections.clear();

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
