package com.scholarly.data.dao;

import com.scholarly.data.model.SubSection;
import com.scholarly.data.util.SyllabusDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubSectionDao {

    private static final String idColumn = "_id";
    private static final String contentColumn = "content";
    private static final String sectionColumn = "section_id";
    private static final String webViewColumn = "is_webview";
    private static final String imageUrlColumn = "image_url";




    public static ObservableList<SubSection> getSubSections(String tableName, int sectionId) {
        ObservableList<SubSection> sections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName + " WHERE section_id = " + sectionId;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            sections.clear();
            while (rs.next()) {
                sections.add(new SubSection(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(sectionColumn),
                        rs.getInt(webViewColumn),
                        rs.getString(imageUrlColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub sections from database ");
            sections.clear();
        }

        return sections;
    }

    public static ObservableList<SubSection> getSubSections(String tableName) {
        ObservableList<SubSection> sections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            sections.clear();
            while (rs.next()) {
                sections.add(new SubSection(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(sectionColumn),
                        rs.getInt(webViewColumn),
                        rs.getString(imageUrlColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sub sections from database ");
            sections.clear();
        }

        return sections;
    }

}
