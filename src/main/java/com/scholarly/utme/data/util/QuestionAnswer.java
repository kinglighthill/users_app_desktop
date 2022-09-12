package com.scholarly.utme.data.util;

public class QuestionAnswer {

    private int id;
    private String answerText;
    private String explanation;

    public QuestionAnswer(int id, String answerText, String explanation) {
        this.id = id;
        this.answerText = answerText;
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getExplanation() {
        return explanation;
    }
}
