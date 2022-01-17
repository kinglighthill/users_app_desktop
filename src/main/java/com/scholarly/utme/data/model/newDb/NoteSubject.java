package com.scholarly.utme.data.model.newDb;

public class NoteSubject {
    private int id;
    private int subjectId;
    private int order;

    public NoteSubject(int id, int subjectId, int order) {
        this.id = id;
        this.subjectId = subjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getOrder() {
        return order;
    }
}
