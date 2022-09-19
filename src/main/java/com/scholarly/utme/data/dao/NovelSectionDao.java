package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.newDb.Section;
import com.scholarly.utme.data.model.novels.ChapterSection;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NovelSectionDao {
    private static final String TAG = "NovelSectionDao: ";

    private static final String idColumn = "_id";
    private static final String contentColumn = "content";
    private static final String parentSectionIdColumn = "parent_section_id";
    private static final String mainSectionOrderColumn = "main_section_order";
    private static final String childSectionOrderColumn = "child_section_order";
    private static final String contentViewTypeColumn = "content_view_type_id";
    private static final String chapterIdColumn = "chapter_id";



    public static ObservableList<ChapterSection> getSections(int chapterId) {
        List<ChapterSection> chapterSections = new ArrayList<>();

        String query1 = "SELECT * FROM " + Tables.NOVEL_SECTIONS + " WHERE chapter_id = " + chapterId;
//        System.out.println(TAG + "Query -> " + query1);

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query1);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ChapterSection section = new ChapterSection(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeColumn),
                        rs.getInt(chapterIdColumn)
                );

                chapterSections.add(section);

            }
//            System.out.println(TAG + "Got chapter sections of size -> " + chapterSections.size());

            return FXCollections.observableArrayList(chapterSections);

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load chapter sections from database because " + e.getMessage());
            chapterSections.clear();

            return null;
        }

    }

}
