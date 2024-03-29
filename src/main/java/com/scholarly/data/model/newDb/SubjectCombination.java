package com.scholarly.data.model.newDb;

public class SubjectCombination {
    private int id;
    private int subjectId;
    private String userId;

    public SubjectCombination(int id, int subjectId, String userId) {
        this.id = id;
        this.subjectId = subjectId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getUserId() {
        return userId;
    }
}
