package com.scholarly.utme.data.model;

public class MediaSubTopic {
    private int id;
    private String title;
    private int order;
    private int topicId;
    private int sectionId;

    public MediaSubTopic(int id, String title, int order, int topicId, int sectionId) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.topicId = topicId;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getSectionId() {
        return sectionId;
    }

    @Override
    public String toString() {
        return title;
    }
}
