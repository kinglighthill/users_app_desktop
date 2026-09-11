package com.scholarly.data.model.newDb.contentType.cbt;

public class CBTBody {
    private int subjectId;
    private int topicId;
    private int questionId;

    public CBTBody(int subjectId, int topicId, int questionId) {
        this.subjectId = subjectId;
        this.topicId = topicId;
        this.questionId = questionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
