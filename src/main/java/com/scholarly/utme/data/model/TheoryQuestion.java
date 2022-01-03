package com.scholarly.utme.data.model;

public class TheoryQuestion implements Question {

    private int Id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private int questionNumber;
    private int questionDescriptionId;
    private String question;
    private String optionAnswer;
    private String answerExplanation;
    private int isExplanationWebView;
    private int isQuestionWebView;

    public TheoryQuestion(int id, int subjectId, int yearId, int topicId, int questionNumber, int questionDescriptionId, String question, String optionAnswer, String answerExplanation, int isExplanationWebView, int isQuestionWebView) {
        Id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.questionNumber = questionNumber;
        this.questionDescriptionId = questionDescriptionId;
        this.question = question;
        this.optionAnswer = optionAnswer;
        this.answerExplanation = answerExplanation;
        this.isExplanationWebView = isExplanationWebView;
        this.isQuestionWebView = isQuestionWebView;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getYearId() {
        return yearId;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getQuestionDescriptionId() {
        return questionDescriptionId;
    }

    public void setQuestionDescriptionId(int questionDescriptionId) {
        this.questionDescriptionId = questionDescriptionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionAnswer() {
        return optionAnswer;
    }

    public void setOptionAnswer(String optionAnswer) {
        this.optionAnswer = optionAnswer;
    }

    public String getAnswerExplanation() {
        return answerExplanation;
    }

    public void setAnswerExplanation(String answerExplanation) {
        this.answerExplanation = answerExplanation;
    }

    public int getIsExplanationWebView() {
        return isExplanationWebView;
    }

    public void setIsExplanationWebView(int isExplanationWebView) {
        this.isExplanationWebView = isExplanationWebView;
    }

    public int getIsQuestionWebView() {
        return isQuestionWebView;
    }

    public void setIsQuestionWebView(int isQuestionWebView) {
        this.isQuestionWebView = isQuestionWebView;
    }
}
