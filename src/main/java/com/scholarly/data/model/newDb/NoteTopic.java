package com.scholarly.data.model.newDb;

public class NoteTopic {
    private int id;
    private String title;
    private int topicId;
    private int subjectId;
    private int order;
    private boolean free;

    public NoteTopic(int id, String title, int topicId, int subjectId, int order, boolean free) {
        this.id = id;
        this.title = title;
        this.topicId = topicId;
        this.subjectId = subjectId;
        this.order = order;
        this.free = free;
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

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
