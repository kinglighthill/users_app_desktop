package com.scholarly.data.model.newDb;

public class SubjectCombination {
    private final int id;
    private final int subjectId;
    private final String userId;

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
