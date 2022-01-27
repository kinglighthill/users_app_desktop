package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Section;
import com.scholarly.utme.data.model.Topic;
import com.scholarly.utme.data.util.SyllabusDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SectionDao {

    private static final String idColumn = "_id";
    private static final String contentColumn = "content";
    private static final String topicColumn = "topic_id";
    private static final String webViewColumn = "is_webview";
    private static final String imageUrlColumn = "image_url";


    public static ObservableList<Section> getSections(String tableName) {
        ObservableList<Section> sections = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = SyllabusDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            sections.clear();
            while (rs.next()) {
                sections.add(new Section(
                        rs.getInt(idColumn),
                        rs.getString(contentColumn),
                        rs.getInt(topicColumn),
                        rs.getInt(webViewColumn),
                        rs.getString(imageUrlColumn)));
            }


            return sections;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load sections from database ");
            sections.clear();

            return null;
        }
    }

}
