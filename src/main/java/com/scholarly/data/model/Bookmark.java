package com.scholarly.data.model;

public class Bookmark {
    private int id;
    private int questionId;
    private int subjectId;
    private int yearId;


    public Bookmark(int id, int questionId, int subjectId, int yearId) {
        this.id = id;
        this.questionId = questionId;
        this.subjectId = subjectId;
        this.yearId = yearId;
    }


    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getYearId() {
        return yearId;
    }
}
