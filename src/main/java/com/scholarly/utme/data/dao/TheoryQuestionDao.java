package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.TheoryQuestion;
import com.scholarly.utme.data.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.scholarly.utme.data.dao.ObjectiveQuestionDao.removeBracketsFromArray;

public class TheoryQuestionDao {
    private static final String TAG = "TheoryQuestionDao: ";

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



    public static ObservableList<TheoryQuestion> getQuestions(int subjectId, int yearId, ObservableList<Integer> topicIdList,  boolean shuffled) {
        ObservableList<TheoryQuestion> questions = FXCollections.observableArrayList();

        String topicIdClause = "";

        if (!topicIdList.isEmpty()) {
            topicIdClause = " AND topic_id IN (" + removeBracketsFromArray(topicIdList) + ")";
        }

        String query;

        if (!shuffled) {
            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause;
        } else {
            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause + " ORDER BY RANDOM()";
        }

//        System.out.println(TAG + "Query = " + query);

        try (Connection connection = NewDatabase.connect()) {
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
                        new QuestionAnswer(-1, rs.getString(optionAnswerColumn), rs.getString(answerExplanationColumn)),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn)));
            }

//            System.out.println(TAG + "Got Theory questions with size -> " + questions.size());

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
