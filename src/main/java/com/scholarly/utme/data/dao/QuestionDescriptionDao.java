package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.QuestionDescription;
import com.scholarly.utme.data.util.DbConnection;
import com.scholarly.utme.data.util.NewDatabase;
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
import java.util.stream.Collectors;

public class QuestionDescriptionDao {
    private static final String TAG = "QuestionDescriptionDao: ";

    private static final String idColumn = "_id";
    private static final String descriptionColumn = "description";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String createdAtColumn = "created_at";

    private static final ObservableList<QuestionDescription> questionDescriptions;

    static {
        questionDescriptions = FXCollections.observableArrayList();
        updateQuestionDescriptionsFromDb();
    }

    private static void updateQuestionDescriptionsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_QUES_DESCRIPTIONS;

        try {
            Connection connection = DbConnection.getDbConnection();
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

//            System.out.println(TAG + "Got questions descriptions with size -> " + questionDescriptions.size());

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Question descriptions from database because " + e.getMessage());
            questionDescriptions.clear();

        }
    }


    public static ObservableList<QuestionDescription> getQuestionDescriptions(int subjectId, int yearId) {

        return FXCollections.observableArrayList(
                questionDescriptions.stream().filter(questionDescription ->
                        questionDescription.getSubjectId() == subjectId && questionDescription.getYearId() == yearId).collect(Collectors.toList())
        );

    }
}
