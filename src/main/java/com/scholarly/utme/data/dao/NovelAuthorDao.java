package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.NovelsDatabase;
import com.scholarly.utme.data.util.Tables;
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
    private static final String TAG = "NovelAuthorDao: ";

    private static final String idColumn = "_id";
    private static final String nameColumn = "name";
    private static final String novelIdColumn = "novel_id";

    private static final ObservableList<NovelAuthor> novelAuthors;

    static {
        novelAuthors = FXCollections.observableArrayList();
        updateNovelAuthorsFromDb();
    }

    private static void updateNovelAuthorsFromDb() {
        String query = "SELECT * FROM " + Tables.NOVEL_AUTHORS;

        try {
            Connection connection = DbConnection.getDbConnection();
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
                    LocalDateTime.now() + ": Could not load Novels from database because " + e.getMessage());
            novelAuthors.clear();
        }
    }

    public static ObservableList<NovelAuthor> getAuthors() {
        return FXCollections.unmodifiableObservableList(novelAuthors);
    }

}
