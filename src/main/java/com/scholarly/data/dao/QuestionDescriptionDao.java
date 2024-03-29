package com.scholarly.data.dao;

import com.scholarly.data.util.DatabaseService;
import com.scholarly.data.model.QuestionDescription;
import com.scholarly.data.model.newDb.ObjectiveQuestionDescription;
import com.scholarly.data.model.newDb.TheoryQuestionDescription;
import com.scholarly.data.util.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QuestionDescriptionDao {
    private static final String TAG = "QuestionDescriptionDao: ";

    private static final DatabaseService databaseService = new DatabaseService();

    private static final String idColumn = "_id";
    private static final String descriptionColumn = "description";
    private static final String subjectIdColumn = "subject_id";
    private static final String yearIdColumn = "year_id";
    private static final String createdAtColumn = "created_at";

    private static final ObservableList<QuestionDescription> questionDescriptions;
    private static final ObservableList<ObjectiveQuestionDescription> objectiveQuestionDescriptions;
    private static final ObservableList<TheoryQuestionDescription> theoryQuestionDescriptions;

    static {
        questionDescriptions = FXCollections.observableArrayList();
        objectiveQuestionDescriptions = FXCollections.observableArrayList();
        theoryQuestionDescriptions = FXCollections.observableArrayList();
        updateQuestionDescriptionsFromDb();
        updateObjectiveQuestionDescriptionsFromDb();
        updateTheoryQuestionDescriptionsFromDb();
    }

    private static void updateObjectiveQuestionDescriptionsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUES_DESCRIPTIONS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            objectiveQuestionDescriptions.clear();

            while (rs.next()) {
                objectiveQuestionDescriptions.add(new ObjectiveQuestionDescription(
                        rs.getInt(idColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn)));
            }

//            System.out.println(TAG + "Got questions descriptions with size -> " + questionDescriptions.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Objective Question Descriptions from database because " + e.getMessage());
            objectiveQuestionDescriptions.clear();

        }
    }

    private static void updateTheoryQuestionDescriptionsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_THEORY_QUES_DESCRIPTIONS;

        try (ResultSet rs = databaseService.executeQuery(query)) {
            theoryQuestionDescriptions.clear();

            while (rs.next()) {
                theoryQuestionDescriptions.add(new TheoryQuestionDescription(
                        rs.getInt(idColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn),
                        rs.getString(createdAtColumn)));
            }

//            System.out.println(TAG + "Got questions descriptions with size -> " + questionDescriptions.size());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Theory Question Descriptions from database because " + e.getMessage());
            theoryQuestionDescriptions.clear();

        }
    }

    private static void updateQuestionDescriptionsFromDb() {
        String query = "SELECT * FROM " + Tables.PQ_OBJECTIVE_QUES_DESCRIPTIONS;

        try(ResultSet rs = databaseService.executeQuery(query)) {
            questionDescriptions.clear();
            while (rs.next()) {
                questionDescriptions.add(new QuestionDescription(
                        rs.getInt(idColumn),
                        rs.getString(descriptionColumn),
                        rs.getInt(subjectIdColumn),
                        rs.getInt(yearIdColumn)));
            }

//            System.out.println(TAG + "Got questions descriptions with size -> " + questionDescriptions.size());

        } catch (Exception e) {
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

    public static ObservableList<ObjectiveQuestionDescription> getObjectiveQuestionDescriptions(int subjectId, int yearId) {

        return FXCollections.observableArrayList(
                objectiveQuestionDescriptions.stream().filter(questionDescription ->
                        questionDescription.getSubjectId() == subjectId && questionDescription.getYearId() == yearId).collect(Collectors.toList())
        );

    }

    public static ObservableList<TheoryQuestionDescription> getTheoryQuestionDescriptions(int subjectId, int yearId) {

        return FXCollections.observableArrayList(
                theoryQuestionDescriptions.stream().filter(questionDescription ->
                        questionDescription.getSubjectId() == subjectId && questionDescription.getYearId() == yearId).collect(Collectors.toList())
        );

    }
}
