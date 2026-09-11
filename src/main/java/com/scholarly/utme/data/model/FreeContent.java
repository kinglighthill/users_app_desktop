package com.scholarly.utme.data.model;

public class FreeContent {
    private int id;
    private int subjectId;
    private int yearId;
    private int topicId;
    private int chapterId;

    public FreeContent(int id, int subjectId, int yearId, int topicId, int chapterId) {
        this.id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.chapterId = chapterId;
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

    public int getChapterId() {
        return chapterId;
    }
}
