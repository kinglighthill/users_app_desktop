package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.util.UserDataDatabase;
import com.scholarly.utme.data.util.UserDatabaseCRUDHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NoteDao {

    private static final String tableName = "notes";

    private static final String idColumn = "id";
    private static final String noteTableNameColumn = "note_table_name";
    private static final String noteIdColumn = "noteId";
    private static final String noteColumn = "note";


    private static final ObservableList<Note> allNotes;

    static {
        allNotes = FXCollections.observableArrayList();
        updateNotesFromDB();
    }


    public static ObservableList<Note> getNotes(String noteTableName) {
        ObservableList<Note> result = FXCollections.observableArrayList();

        String query;


        query = "SELECT * FROM " + tableName;

        try (Connection connection = UserDataDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                result.add(new Note(
                        rs.getInt(idColumn),
                        rs.getString(noteTableNameColumn),
                        rs.getInt(noteIdColumn),
                        rs.getString(noteColumn)));
            }

            System.out.println("Got notes of length -> " + result.size());

            return FXCollections.observableList(result.stream().filter(notes -> Objects.equals(notes.getNoteTableName(), noteTableName)).collect(Collectors.toList()));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load notes from database ");

            return null;
        }
    }

    public static int deleteNote(int id) {
        int deletedId = UserDatabaseCRUDHelper.delete(tableName, id);

        System.out.println("Note of id -> " + deletedId + " has been deleted");

        return deletedId;
    }

    public static int createNote(String noteTableName, int noteId, String note) {
        int id = (int) UserDatabaseCRUDHelper.create(
                tableName,
                new String[]{"note_table_name", "noteId", "note"},
                new Object[]{noteTableName, noteId, note},
                new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR});

        System.out.println("Note created with id -> " + id);
        return id;
    }

    public static int updateNote(Note note) {
        int rows = UserDatabaseCRUDHelper.update(
                tableName,
                new String[]{noteTableNameColumn, noteIdColumn, noteColumn},
                new Object[]{note.getNoteTableName(), note.getNoteId(), note.getNote()},
                new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR},
                idColumn,
                Types.INTEGER,
                note.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Note to be updated with id " + note.getId() + " didn't exist in database");

        return rows;
    }

    private static void updateNotesFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = UserDataDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            allNotes.clear();
            while (rs.next()) {
                allNotes.add(new Note(
                        rs.getInt(idColumn),
                        rs.getString(noteTableNameColumn),
                        rs.getInt(noteIdColumn),
                        rs.getString(noteColumn)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Notes from database because " + e.getMessage());
            allNotes.clear();
        }
    }

    public static ObservableList<Note> getAllNotes() {
        return FXCollections.unmodifiableObservableList(allNotes);
    }

    public static Optional<Note> getHighlight(int id) {
        for (Note note : allNotes) {
            if (note.getId() == id) return Optional.of(note);
        }
        return Optional.empty();
    }

}
