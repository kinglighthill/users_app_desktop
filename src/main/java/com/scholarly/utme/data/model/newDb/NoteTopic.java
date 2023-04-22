package com.scholarly.utme.data.model.newDb;

public class NoteTopic {
    private int id;
    private String title;
    private int topicId;
    private int subjectId;
    private int order;

    public NoteTopic(int id, String title, int topicId, int subjectId, int order) {
        this.id = id;
        this.title = title;
        this.topicId = topicId;
        this.subjectId = subjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getOrder() {
        return order;
    }
}
