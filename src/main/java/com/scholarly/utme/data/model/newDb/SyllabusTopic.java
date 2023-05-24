package com.scholarly.utme.data.model.newDb;

public class SyllabusTopic {
    private int id;
    private int sectionId;
    private int categoryId;
    private int order;

    public SyllabusTopic(int id, int sectionId, int subjectId, int order) {
        this.id = id;
        this.sectionId = sectionId;
        this.categoryId = subjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getOrder() {
        return order;
    }
}
