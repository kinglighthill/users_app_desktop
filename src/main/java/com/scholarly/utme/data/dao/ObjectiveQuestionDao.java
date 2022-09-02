package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.util.Database;
import com.scholarly.utme.data.util.NewDatabase;
import com.scholarly.utme.data.util.Table;
import com.scholarly.utme.data.util.Tables;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectiveQuestionDao {
    public static final String TAG = "ObjectiveQuestionDao: ";

    private static final String idColumn = "_id";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String topicIdColumn = "topic_id";
    private static final String questionNumberColumn = "question_num";
    private static final String questionDescriptionIdColumn = "ques_desc_id";
    private static final String questionColumn = "question";
    private static final String optionAColumn = "option_a";
    private static final String optionBColumn = "option_b";
    private static final String optionCColumn = "option_c";
    private static final String optionDColumn = "option_d";
    private static final String optionEColumn = "option_e";
    private static final String optionAnswerColumn = "option_answer";
    private static final String answerExplanationColumn = "answer_explanation";
    private static final String optionAnswerIdColumn = "option_answer_id";
    private static final String isExplanationWebViewColumn = "is_exp_webview";
    private static final String isQuestionWebViewColumn = "is_ques_webview";
    private static final String isGammableColumn = "is_gammable";


    public static ObservableList<ObjectiveQuestion> getQuestions(int subjectId, int yearId, boolean shuffled) {
        ObservableList<ObjectiveQuestion> questions = FXCollections.observableArrayList();

        String query;

        if (!shuffled) {
            query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId;
            System.out.println(TAG + "Query = " + query);
//            query = "SELECT * FROM " + tableName + " WHERE year_id = " + yearId;
        } else {
            query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + " ORDER BY RANDOM()";
//            query = "SELECT * FROM " + tableName + " WHERE year_id = " + yearId + " ORDER BY RANDOM()";
        }

        try (Connection connection = NewDatabase.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet rs = statement.executeQuery();
            questions.clear();
            while (rs.next()) {
                questions.add(new ObjectiveQuestion(
                        rs.getInt(idColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getInt(topicIdColumn),
                        rs.getInt(questionNumberColumn),
                        rs.getInt(questionDescriptionIdColumn),
                        rs.getString(questionColumn),
                        rs.getString(optionAColumn),
                        rs.getString(optionBColumn),
                        rs.getString(optionCColumn),
                        rs.getString(optionDColumn),
                        rs.getString(optionEColumn),
                        rs.getString(optionAnswerColumn),
                        rs.getString(answerExplanationColumn),
                        rs.getInt(optionAnswerIdColumn),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn),
                        rs.getInt(isGammableColumn)));
            }

            System.out.println(TAG + "Got questions with size -> " + questions.size());

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
