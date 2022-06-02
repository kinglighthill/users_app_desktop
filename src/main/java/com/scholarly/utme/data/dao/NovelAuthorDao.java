package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelAuthor;
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

public class NovelAuthorDao {

    private static final String tableName = "novel_authors";

    private static final String idColumn = "_id";
    private static final String nameColumn = "name";
    private static final String novelIdColumn = "novel_id";

    private static final ObservableList<NovelAuthor> novelAuthors;

    static {
        novelAuthors = FXCollections.observableArrayList();
        updateNovelAuthorsFromDb();
    }

    private static void updateNovelAuthorsFromDb() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = NovelsDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            novelAuthors.clear();
            while (rs.next()) {
                novelAuthors.add(new NovelAuthor(
                        rs.getInt(idColumn),
                        rs.getString(nameColumn),
                        rs.getInt(novelIdColumn)));
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novels from database ");
            novelAuthors.clear();
        }
    }

    public static ObservableList<NovelAuthor> getAuthors() {
        return FXCollections.unmodifiableObservableList(novelAuthors);
    }

}
