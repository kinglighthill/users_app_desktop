package com.scholarly.utme.data.model.newDb;

public class NoteSubTopic {
    private int id;
    private String title;
    private int topicId;
    private int order;

    public NoteSubTopic(int id, String title, int topicId, int order) {
        this.id = id;
        this.title = title;
        this.topicId = topicId;
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

    public int getOrder() {
        return order;
    }
}
