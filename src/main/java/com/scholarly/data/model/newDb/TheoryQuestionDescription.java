package com.scholarly.data.model.newDb;

import com.scholarly.data.model.QuestionDescription;

public class TheoryQuestionDescription extends QuestionDescription {
    private String createdAt;

    public TheoryQuestionDescription(int id, String description, int subjectId, int yearId, String createdAt) {
        super(id, description, subjectId, yearId);
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
