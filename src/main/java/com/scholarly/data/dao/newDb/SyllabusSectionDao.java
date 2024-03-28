package com.scholarly.data.dao.newDb;

import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.newDb.SyllabusSection;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyllabusSectionDao {
    private static final String TAG = "SyllabusCategoryDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String contentColumn = "content";
    private static final String objectivesColumn = "objectives";
    private static final String parentSectionIdColumn = "parent_section_id";
    private static final String mainSectionOrderColumn = "main_section_order";
    private static final String childSectionOrderColumn = "child_section_order";


    public static ObservableList<SyllabusSection> getSections(int subjectId) {
        ObservableList<SyllabusSection> sections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.SYLLABUS_SECTIONS + " WHERE " + subjectIdColumn + " = " + subjectId;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            sections.clear();
            while (rs.next()){
                sections.add(new SyllabusSection(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getString(contentColumn),
                        rs.getString(objectivesColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn)));
            }
            //System.out.println("Got category from DB" + tableName + ": " + categories);
            return sections;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load categories from database ");
            sections.clear();

            return null;
        }
    }
}
