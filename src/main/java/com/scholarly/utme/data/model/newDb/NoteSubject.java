package com.scholarly.utme.data.model.newDb;

public class NoteSubject {
    private int id;
    private String title;
    private int subjectId;
    private int order;

    public NoteSubject(int id, String title, int subjectId, int order) {
        this.id = id;
        this.title = title;
        this.subjectId = subjectId;
        this.order = order;
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

    public int getOrder() {
        return order;
    }
}
