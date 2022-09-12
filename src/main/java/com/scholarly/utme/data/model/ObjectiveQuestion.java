package com.scholarly.utme.data.model;

import com.scholarly.utme.data.util.QuestionAnswer;
import com.scholarly.utme.data.util.QuestionOption;

public class ObjectiveQuestion implements Question {

    private int id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private int questionNumber;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String optionAnswer;
    private String answerExplanation;
    private int optionAnswerId;
    private int questionDescriptionId;
    private int isExplanationWebView;
    private int isQuestionWebView;
    private int gammable;


    private QuestionOption questionOptionA;
    private QuestionOption questionOptionB;
    private QuestionOption questionOptionC;
    private QuestionOption questionOptionD;
    private QuestionOption questionOptionE;

    private QuestionAnswer questionAnswer;

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

    public ObjectiveQuestion(int id, int subjectId, int yearId, int topicId, int questionNumber, int questionDescriptionId, String question, QuestionOption optionA, QuestionOption optionB, QuestionOption optionC, QuestionOption optionD, QuestionOption optionE, QuestionAnswer optionAnswer, int isExplanationWebView, int isQuestionWebView, int gammable) {
        this.id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.questionNumber = questionNumber;
        this.questionDescriptionId = questionDescriptionId;
        this.question = question;
        this.questionOptionA = optionA;
        this.questionOptionB = optionB;
        this.questionOptionC = optionC;
        this.questionOptionD = optionD;
        this.questionOptionE = optionE;
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

    public String getOptionA() {
        return optionA;
    }

    public QuestionOption getQuestionOptionA () {
        return questionOptionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public QuestionOption getQuestionOptionB() {
        return questionOptionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public QuestionOption getQuestionOptionC() {
        return questionOptionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public QuestionOption getQuestionOptionD() {
        return questionOptionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public QuestionOption getQuestionOptionE() {
        return questionOptionE;
    }

    public String getOptionAnswer() {
        return optionAnswer;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public String getAnswerExplanation() {
        return answerExplanation;
    }

    /*public int getOptionAnswerId() {
        return optionAnswerId;
    }*/

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
