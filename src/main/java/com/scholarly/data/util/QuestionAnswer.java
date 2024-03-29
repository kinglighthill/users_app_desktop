package com.scholarly.data.util;

public class QuestionAnswer {

    private int id;
    private String answer;
    private String explanation;

    public QuestionAnswer(int id, String answer, String explanation) {
        this.id = id;
        this.answer = answer;
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }
}
