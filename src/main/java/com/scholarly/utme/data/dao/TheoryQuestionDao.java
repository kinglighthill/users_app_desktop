package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.TheoryQuestion;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheoryQuestionDao {

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String topicIdColumn = "topic_id";
    private static final String questionNumberColumn = "question_num";
    private static final String questionDescriptionIdColumn = "ques_desc_id";
    private static final String questionColumn = "question";
    private static final String optionAnswerColumn = "option_answer";
    private static final String answerExplanationColumn = "answer_explanation";
    private static final String isExplanationWebViewColumn = "is_exp_webview";
    private static final String isQuestionWebViewColumn = "is_ques_webview";



    public static ObservableList<TheoryQuestion> getQuestions(String tableName, int yearId, boolean shuffled) {
        ObservableList<TheoryQuestion> questions = FXCollections.observableArrayList();

        String query;

        if (!shuffled) {
            query = "SELECT * FROM " + tableName + " WHERE year_id = " + yearId;
        } else {
            query = "SELECT * FROM " + tableName + " WHERE year_id = " + yearId + " ORDER BY RAND()";
        }

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            questions.clear();
            while (rs.next()) {
                questions.add(new TheoryQuestion(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(questionNumberColumn),
                        rs.getInt(questionDescriptionIdColumn),
                        rs.getString(questionColumn),
                        rs.getString(optionAnswerColumn),
                        rs.getString(answerExplanationColumn),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn)));
            }


            return questions;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Subjects from database ");
            questions.clear();

            return null;
        }
    }
}
