package com.scholarly.utme.data.util;

public class QuestionOption {
    private int id;
    private String text;

    public QuestionOption(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
