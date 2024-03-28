package com.scholarly.data.model;

public class ObjectiveBookmark {
    private int id;
    private int subjectId;
    private int yearId;
    private int questionId;
    private String createdAt;

    public ObjectiveBookmark(int id, int subjectId, int yearId, int questionId, String createdAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.questionId = questionId;
        this.createdAt = createdAt;
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

    public int getQuestionId() {
        return questionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    /*@Override
    public String toString() {
        return  "[ SubjectID: " + subjectId + " YearID: " + yearId + " QuestionID: " + questionId + "]";
    }*/
}
