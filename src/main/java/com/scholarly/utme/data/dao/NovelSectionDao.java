package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.novels.ChapterSection;
import com.scholarly.utme.data.util.DbConnection;
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
import java.util.stream.Collectors;

public class NovelSectionDao {
    private static final String TAG = "NovelSectionDao: ";

    private static final String idColumn = "_id";
    private static final String contentColumn = "content";
    private static final String parentSectionIdColumn = "parent_section_id";
    private static final String mainSectionOrderColumn = "main_section_order";
    private static final String childSectionOrderColumn = "child_section_order";
    private static final String contentViewTypeIdColumn = "content_view_type_id";
    private static final String chapterIdColumn = "chapter_id";

    private static final List<ChapterSection> novelChapterSections;

    static {
        novelChapterSections = FXCollections.observableArrayList();
        updateNovelChapterSectionsFromDb();
    }


    private static void updateNovelChapterSectionsFromDb() {
        String query = "SELECT * FROM " + Tables.NOVEL_SECTIONS;

        try {
            Connection connection = DbConnection.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            novelChapterSections.clear();
            while (rs.next()) {
                novelChapterSections.add(new ChapterSection(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(parentSectionIdColumn),
                        rs.getInt(mainSectionOrderColumn),
                        rs.getInt(childSectionOrderColumn),
                        rs.getInt(contentViewTypeIdColumn),
                        rs.getInt(chapterIdColumn)));

            }
//            System.out.println(TAG + "Got chapter sections of size -> " + chapterSections.size());

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load novel chapter sections from database because " + e.getMessage());
            novelChapterSections.clear();
        }
    }

    public static ObservableList<ChapterSection> getSections(int chapterId) {

        return FXCollections.observableArrayList(
                novelChapterSections.stream().filter(chapterSection ->
                chapterSection.getChapterId() == chapterId).collect(Collectors.toList())
        );

    }

}
