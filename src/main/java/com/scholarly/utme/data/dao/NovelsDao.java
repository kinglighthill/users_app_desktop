package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.util.NovelsDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NovelsDao {

    private static final String tableName = "novels";

    private static final String idColumn = "_id";
    private static final String imagePathColumn = "image_path";
    private static final String nameColumn = "name";
    private static final String summaryColumn = "summary";
    private static final String aboutColumn = "about";
    private static final String chaptersCountColumn = "chapters_count";
    private static final String genreIdColumn = "genre_id";
    private static final String typeIdColumn = "type_id";
    private static final String divisionIdColumn = "division_id";
    private static final String positionColumn = "position";
    private static final String isNewColumn = "is_new";
    private static final String availableColumn = "available";
    private static final String creditIdColumn = "credit_id";

    private static final ObservableList<Novel> novels;

    static {
        novels = FXCollections.observableArrayList();
        updateNovelsFromDb();
    }

    private static void updateNovelsFromDb() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = NovelsDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            novels.clear();
            while (rs.next()) {
                novels.add(new Novel(
                        rs.getInt(idColumn),
                        rs.getString(imagePathColumn),
                        rs.getString(nameColumn),
                        rs.getString(summaryColumn),
                        rs.getString(aboutColumn),
                        rs.getInt(chaptersCountColumn),
                        rs.getInt(genreIdColumn),
                        rs.getInt(typeIdColumn),
                        rs.getInt(divisionIdColumn),
                        rs.getInt(positionColumn),
                        rs.getInt(isNewColumn),
                        rs.getInt(availableColumn),
                        rs.getInt(creditIdColumn)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novels from database ");
            novels.clear();
        }
    }

    public static ObservableList<Novel> getNovels() {
        return FXCollections.unmodifiableObservableList(novels);
    }

}
