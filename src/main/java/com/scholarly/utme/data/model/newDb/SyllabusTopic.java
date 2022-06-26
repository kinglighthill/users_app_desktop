package com.scholarly.utme.data.model.newDb;

public class SyllabusTopic {
    private int id;
    private String title;
    private int categoryId;
    private int order;

    public SyllabusTopic(int id, String title, int subjectId, int order) {
        this.id = id;
        this.title = title;
        this.categoryId = subjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getOrder() {
        return order;
    }
}
