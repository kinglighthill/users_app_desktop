package com.scholarly.utme.data.dao;

import com.scholarly.utme.data.DatabaseService;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.novels.NovelObjectiveQuestion;
import com.scholarly.utme.data.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ObjectiveQuestionDao {
    private static final String TAG = "ObjectiveQuestionDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

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
    private static final String isGameAbleColumn = "is_game_able";

    private static final String novelIdColumn = "novel_id";
    private static final String chapterIdColumn = "chapter_id";

    private static final ObservableList<NovelObjectiveQuestion> novelQuestions;

    static {
        novelQuestions = FXCollections.observableArrayList();
        updateNovelQuestions();
    }


    public static ObservableList<ObjectiveQuestion> getQuestions(int subjectId, int yearId, ObservableList<Integer> topicIdList, boolean shuffled) {
        ObservableList<ObjectiveQuestion> questions = FXCollections.observableArrayList();

        String topicIdClause = "";

        if (!topicIdList.isEmpty()) {
            topicIdClause = " AND topic_id IN (" + removeBracketsFromArray(topicIdList) + ")";
        }

        String query;

        if (!shuffled) {
            query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause;
        } else {
            query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUESTIONS + " WHERE subject_id = " + subjectId + " AND year_id = " + yearId + topicIdClause + " ORDER BY RANDOM()";
        }

//        System.out.println(TAG + "Query = " + query);

        try (ResultSet rs = databaseService.executeQuery(query)){
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
                        new QuestionOption(0, rs.getString(optionAColumn)),
                        new QuestionOption(1, rs.getString(optionBColumn)),
                        new QuestionOption(2, rs.getString(optionCColumn)),
                        new QuestionOption(3, rs.getString(optionDColumn)),
                        new QuestionOption(4, rs.getString(optionEColumn)),
                        new QuestionAnswer(rs.getInt(optionAnswerIdColumn), rs.getString(optionAnswerColumn), rs.getString(answerExplanationColumn)),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn),
                        rs.getInt(isGameAbleColumn)));

            }

//            System.out.println(TAG + "Got Objective questions with size -> " + questions.size());

            return questions;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Objective questions from database because " + e.getMessage());
            questions.clear();

            return null;
        }
    }

    public static ObservableList<ObjectiveQuestion> getQuestionsWithNoteSubjectId(int subjectId) {
        ObservableList<ObjectiveQuestion> questions = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUESTIONS + " WHERE subject_id = " + subjectId;

        System.out.println(TAG + "Objective Question Query -> " + query);

        try (ResultSet rs = databaseService.executeQuery(query)){
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
                        new QuestionOption(0, rs.getString(optionAColumn)),
                        new QuestionOption(1, rs.getString(optionBColumn)),
                        new QuestionOption(2, rs.getString(optionCColumn)),
                        new QuestionOption(3, rs.getString(optionDColumn)),
                        new QuestionOption(4, rs.getString(optionEColumn)),
                        new QuestionAnswer(rs.getInt(optionAnswerIdColumn), rs.getString(optionAnswerColumn), rs.getString(answerExplanationColumn)),
                        rs.getInt(isExplanationWebViewColumn),
                        rs.getInt(isQuestionWebViewColumn),
                        rs.getInt(isGameAbleColumn)));

            }

//            System.out.println(TAG + "Got Objective questions with size -> " + questions.size());

            return questions;

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Objective questions from database because " + e.getMessage());
            questions.clear();

            return FXCollections.emptyObservableList();
        }
    }

    private static void updateNovelQuestions() {
        String query = "SELECT * FROM " + Tables.NOVEL_OBJECTIVE_QUESTIONS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            novelQuestions.clear();
            while (rs.next()) {
                novelQuestions.add(new NovelObjectiveQuestion(
                        rs.getInt(idColumn),
                        rs.getInt(novelIdColumn),
                        rs.getInt(chapterIdColumn),
                        rs.getInt(questionNumberColumn),
                        rs.getString(questionColumn),
                        new QuestionOption(0, rs.getString(optionAColumn)),
                        new QuestionOption(1, rs.getString(optionBColumn)),
                        new QuestionOption(2, rs.getString(optionCColumn)),
                        new QuestionOption(3, rs.getString(optionDColumn)),
                        new QuestionOption(4, rs.getString(optionEColumn)),
                        new QuestionAnswer(rs.getInt(optionAnswerIdColumn), rs.getString(optionAnswerColumn), rs.getString(answerExplanationColumn)),
                        rs.getInt(questionDescriptionIdColumn)));
            }

            System.out.println(TAG + "Got Novel Objective questions with size -> " + novelQuestions.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Novel Objective Questions from database because " + e.getMessage());
            novelQuestions.clear();

        }
    }

    public static ObservableList<NovelObjectiveQuestion> getNovelQuestions(int chapterId) {
        return FXCollections.observableArrayList(
                novelQuestions.stream().filter(novelObjectiveQuestion ->
                novelObjectiveQuestion.getChapterId() == chapterId).collect(Collectors.toList())
        );
    }

    public static String removeBracketsFromArray(ObservableList<Integer> topicIds) {
        String query;
        query = topicIds.toString().replace("[", "");
        return query.replace("]", "");
    }
}
