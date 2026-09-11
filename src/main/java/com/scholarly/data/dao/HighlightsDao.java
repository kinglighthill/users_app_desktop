package com.scholarly.data.dao;

import com.scholarly.data.model.Highlights;
import com.scholarly.data.util.UserDataDatabase;
import com.scholarly.data.util.UserDatabaseCRUDHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HighlightsDao {

    private static final String tableName = "highlights";

    private static final String idColumn = "id";
    private static final String noteTableNameColumn = "noteTableName";
    private static final String noteIdColumn = "noteId";
    private static final String colorColumn = "color";


    private static final ObservableList<Highlights> allHighlights;

    static {
        allHighlights = FXCollections.observableArrayList();
        updateHighlightsFromDB();
    }


    public static ObservableList<Highlights> getHighlights(String noteTableName) {
        ObservableList<Highlights> result = FXCollections.observableArrayList();

        String query;


        query = "SELECT * FROM " + tableName;

        try (Connection connection = UserDataDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                result.add(new Highlights(
                        rs.getInt(idColumn),
                        rs.getString(noteTableNameColumn),
                        rs.getInt(noteIdColumn),
                        rs.getString(colorColumn)));
            }

            System.out.println("Got highlights of length -> " + result.size());

            return FXCollections.observableList(result.stream().filter(highlights -> Objects.equals(highlights.getNoteTableName(), noteTableName)).collect(Collectors.toList()));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load highlights from database because " + e.getMessage());

            return null;
        }
    }

    public static int deleteHighlight(int id) {
        int deletedId = UserDatabaseCRUDHelper.delete(tableName, id);

        System.out.println("Highlight of id -> " + deletedId + " has been deleted");

        return deletedId;
    }

    public static int createHighlight(String noteTableName, int noteId, String color) {
        int id = (int) UserDatabaseCRUDHelper.create(
                tableName,
                new String[]{"noteTableName", "noteId", "color"},
                new Object[]{noteTableName, noteId, color},
                new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR});

        System.out.println("Highlight created with id -> " + id);
        return id;
    }

    public static int updateHighlight(Highlights highlight) {
        int rows = UserDatabaseCRUDHelper.update(
                tableName,
                new String[]{noteTableNameColumn, noteIdColumn, colorColumn},
                new Object[]{highlight.getNoteTableName(), highlight.getNoteId(), highlight.getColor()},
                new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR},
                idColumn,
                Types.INTEGER,
                highlight.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Highlight to be updated with id " + highlight.getId() + " didn't exist in database");

        return rows;
    }

    private static void updateHighlightsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = UserDataDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            allHighlights.clear();
            while (rs.next()) {
                allHighlights.add(new Highlights(
                        rs.getInt(idColumn),
                        rs.getString(noteTableNameColumn),
                        rs.getInt(noteIdColumn),
                        rs.getString(colorColumn)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Highlights from database ");
            allHighlights.clear();
        }
    }

    public static ObservableList<Highlights> getAllHighlights() {
        return FXCollections.unmodifiableObservableList(allHighlights);
    }

    public static Optional<Highlights> getHighlight(int id) {
        for (Highlights highlight : allHighlights) {
            if (highlight.getId() == id) return Optional.of(highlight);
        }
        return Optional.empty();
    }

}
