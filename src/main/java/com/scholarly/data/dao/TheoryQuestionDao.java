package com.scholarly.data.dao;

import com.scholarly.data.DatabaseService;
import com.scholarly.data.model.TheoryQuestion;
import com.scholarly.data.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.scholarly.data.dao.ObjectiveQuestionDao.removeBracketsFromArray;

public class TheoryQuestionDao {
    private static final String TAG = "TheoryQuestionDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

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
//            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause;
            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId;
        } else {
//            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause + " ORDER BY RANDOM()";
            query = "SELECT * FROM " + Tables.PQ_THEORY_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + " ORDER BY RANDOM()";
        }
//        System.out.println(TAG + "getQuestions Query -> " + query);

        try(ResultSet rs = databaseService.executeQuery(query)) {
            questions.clear();
            while (rs.next()) {
                questions.add(new TheoryQuestion(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(topicIdColumn),
                        rs.getString(questionNumberColumn),
                        rs.getInt(questionDescriptionIdColumn),
                        rs.getString(questionColumn),
                        new QuestionAnswer(-1, rs.getString(optionAnswerColumn), rs.getString(answerExplanationColumn)),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn)));
            }

            System.out.println(TAG + "Got Theory questions with size -> " + questions.size());

            return questions;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Theory Questions from database because " + e.getMessage());
            questions.clear();

            return null;
        }

    }
}
