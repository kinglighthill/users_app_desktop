package com.scholarly.utme.data.model;

public class ObjectiveQuestion {

    private int id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private int questionNumber;
    private int questionDescriptionId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String optionAnswer;
    private String answerExplanation;
    private int optionAnswerId;
    private int isExplanationWebView;
    private int isQuestionWebView;
    private int gammable;

    public ObjectiveQuestion(int id, int subjectId, int yearId, int topicId, int questionNumber, int questionDescriptionId, String question, String optionA, String optionB, String optionC, String optionD, String optionE, String optionAnswer, String answerExplanation, int optionAnswerId, int isExplanationWebView, int isQuestionWebView, int gammable) {
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
        this.optionAnswer = optionAnswer;
        this.answerExplanation = answerExplanation;
        this.optionAnswerId = optionAnswerId;
        this.isExplanationWebView = isExplanationWebView;
        this.isQuestionWebView = isQuestionWebView;
        this.gammable = gammable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
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

    public int getOptionAnswerId() {
        return optionAnswerId;
    }

    public void setOptionAnswerId(int optionAnswerId) {
        this.optionAnswerId = optionAnswerId;
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

    public int getGammable() {
        return gammable;
    }

    public void setGammable(int gammable) {
        this.gammable = gammable;
    }
}
