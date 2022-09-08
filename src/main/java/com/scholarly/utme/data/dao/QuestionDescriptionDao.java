package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.newDb.QuestionDescription;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionDescriptionDao {
    private static final String TAG = "QuestionDescriptionDao: ";

    private static final String idColumn = "_id";
    private static final String descriptionColumn = "description";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String createdAtColumn = "created_at";


    public static ObservableList<QuestionDescription> getQuestionDescriptions(int subjectId, int yearId) {
        ObservableList<QuestionDescription> questionDescriptions = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.PQ_QUES_DESCRIPTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId;

        System.out.println(TAG + "Query = " + query);

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet rs = statement.executeQuery();
            questionDescriptions.clear();
            while (rs.next()) {
                questionDescriptions.add(new QuestionDescription(
                        rs.getInt(idColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getString(createdAtColumn)));
            }

            System.out.println(TAG + "Got questions descriptions with size -> " + questionDescriptions.size());

            return questionDescriptions;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Question descriptions from database because " + e.getMessage());
            questionDescriptions.clear();

            return null;
        }
    }
}
