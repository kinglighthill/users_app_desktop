package com.scholarly.data.model;

import com.scholarly.data.util.QuestionAnswer;

public class TheoryQuestion implements Question {

    private int Id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private String questionNumber;
    private int questionDescriptionId;
    private String question;
    private QuestionAnswer questionAnswer;
    private int isExplanationWebView;
    private int isQuestionWebView;

    public TheoryQuestion(int id, int subjectId, int yearId, int topicId, String questionNumber, int questionDescriptionId, String question, QuestionAnswer questionAnswer, int isExplanationWebView, int isQuestionWebView) {
        Id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.questionNumber = questionNumber;
        this.questionDescriptionId = questionDescriptionId;
        this.question = question;
        this.questionAnswer = questionAnswer;
        this.isExplanationWebView = isExplanationWebView;
        this.isQuestionWebView = isQuestionWebView;
    }

    public int getId() {
        return Id;
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

    public String getQuestionNumber() {
        return questionNumber;
    }

    public int getQuestionDescriptionId() {
        return questionDescriptionId;
    }

    public String getQuestion() {
        return question;
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

}
