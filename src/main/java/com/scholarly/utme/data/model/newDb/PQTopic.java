package com.scholarly.utme.data.model.newDb;

public class PQTopic {
    private int id;
    private String title;
    private int subjectId;

    public PQTopic(int id, String title, int subjectId) {
        this.id = id;
        this.title = title;
        this.subjectId = subjectId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSubjectId() {
        return subjectId;
    }


    @Override
    public String toString() {
        return title;
    }
}
