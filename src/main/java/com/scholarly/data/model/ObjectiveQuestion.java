package com.scholarly.data.model;

import com.scholarly.data.util.QuestionAnswer;
import com.scholarly.data.util.QuestionOption;

public class ObjectiveQuestion implements Question {

    private int id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private int questionNumber;
    private String question;
    private QuestionOption optionA;
    private QuestionOption optionB;
    private QuestionOption optionC;
    private QuestionOption optionD;
    private QuestionOption optionE;
    private QuestionAnswer questionAnswer;
    private int questionDescriptionId;
    private int isExplanationWebView;
    private int isQuestionWebView;
    private int gammable;


    public ObjectiveQuestion(int id, int subjectId, int yearId, int topicId, int questionNumber, int questionDescriptionId, String question, QuestionOption optionA, QuestionOption optionB, QuestionOption optionC, QuestionOption optionD, QuestionOption optionE, QuestionAnswer optionAnswer, int isExplanationWebView, int isQuestionWebView, int gammable) {
        this.id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.questionNumber = questionNumber;
        this.questionDescriptionId = questionDescriptionId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.questionAnswer = optionAnswer;
        this.isExplanationWebView = isExplanationWebView;
        this.isQuestionWebView = isQuestionWebView;
        this.gammable = gammable;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getYearId() {
        return yearId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getQuestionDescriptionId() {
        return questionDescriptionId;
    }

    public String getQuestion() {
        return question;
    }

    public QuestionOption getOptionA() {
        return optionA;
    }

    public QuestionOption getOptionB() {
        return optionB;
    }

    public QuestionOption getOptionC() {
        return optionC;
    }

    public QuestionOption getOptionD() {
        return optionD;
    }

    public QuestionOption getOptionE() {
        return optionE;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public int getIsExplanationWebView() {
        return isExplanationWebView;
    }

    public int getIsQuestionWebView() {
        return isQuestionWebView;
    }

    public int getGammable() {
        return gammable;
    }

}
